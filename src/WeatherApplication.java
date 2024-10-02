import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApplication {

    // method for the first API call which will return location data in a JSON Array
    public static JSONArray getLocationData(String locationName) {

        // replace all spaces with '+' to match API requirements
        locationName = locationName.replace(" ", "+");

        // contains the API url with our user inputted variable between to allow our own personal search
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="
                + locationName + "&count=10&language=en&format=json";


        try {
            // call API and get response
            HttpURLConnection conn = fetchAPIResponse(urlString);

            // response code gathered from connection attempt
            int responseCode = conn.getResponseCode();

            // making sure API call was successful
            if (responseCode != 200) {
                throw new RuntimeException("Error, HttpResponseCode: " + responseCode);
            }
            else{
                StringBuilder stringBuilder = new StringBuilder();

                // Store the API results
                Scanner scanner = new Scanner(conn.getInputStream());

                // Read and store the API data into the StringBuilder
                while(scanner.hasNext()) {
                    stringBuilder.append(scanner.nextLine());
                }

                // close the scanner
                scanner.close();

                // end the API connection
                conn.disconnect();

                // JSONParser to turn JSON file from API into Java JSON Object
                JSONParser parser = new JSONParser();

                // parse our API results stored in StringBuilder to a JSON Object
                JSONObject jsonObjectResults = (JSONObject) parser.parse(String.valueOf(stringBuilder));

                // get list of location data
                JSONArray locationData = (JSONArray) jsonObjectResults.get("results");

                return locationData;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static JSONObject getWeatherData(String locationName) {

        // get data made from first API call
        JSONArray locationData = getLocationData(locationName);

        // within the JSON Array get the first search result
        JSONObject location = (JSONObject) locationData.get(0);

        // values for the location data needed for second API call
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        // Weather API url with our values to make selected request
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitude+"&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";

        try {
            // create API connection
            HttpURLConnection connection = fetchAPIResponse(urlString);

            // checks if connection is valid
            if (connection.getResponseCode()!= 200) {
                throw new RuntimeException("Error, HttpResponseCode: " + connection.getResponseCode());
            }
            // used to store our data generated by API
            StringBuilder stringBuilder = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());

            // stores data in StringBuilder
            while (scanner.hasNext()) {
                stringBuilder.append(scanner.nextLine());
            }

            // close scanner
            scanner.close();

            // end API connection
            connection.disconnect();

            // JSONParser to turn JSON file from API into Java JSON Object
            JSONParser parser = new JSONParser();

            // parse our API results stored in StringBuilder to a JSON Object
            JSONObject weatherJsonResults = (JSONObject) parser.parse(String.valueOf(stringBuilder));

            // JSON Object within our initial Objects with key 'hourly'
            JSONObject hourlyWeatherData = (JSONObject) weatherJsonResults.get("hourly");

            // go into sub Array of 'time' to gather times
            JSONArray timeArray = (JSONArray) hourlyWeatherData.get("time");

            // value for index to match closest to our current time
            int index = getIndexOfCurrentTime(timeArray);

            // Find the corresponding temperature data at that index
            JSONArray temperatureData = (JSONArray) hourlyWeatherData.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            // Find the corresponding weather code data at that index
            JSONArray weatherCode = (JSONArray) hourlyWeatherData.get("weather_code");
            String weatherCondition = getWeatherCode((long)weatherCode.get(index));

            // Find the corresponding humidity data at that index
            JSONArray humidityData = (JSONArray) hourlyWeatherData.get("relative_humidity_2m");
            long humidity = (long) humidityData.get(index);

            // Find the corresponding Wind Speed data at that index
            JSONArray windspeedData = (JSONArray) hourlyWeatherData.get("wind_speed_10m");
            double windspeed = (double) windspeedData.get(index);

            // format all the results into a JSON Object so it can be utilised in the frontend of the app
            JSONObject weatherData = new JSONObject();
            weatherData.put("windspeed", windspeed);
            weatherData.put("humidity", humidity);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("temperature", temperature);

            // return the JSON object of our final data
            return weatherData;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // code for setting up call and connection to API
    private static HttpURLConnection fetchAPIResponse(String urlString) {
        try {
            URL url = new URL(urlString);

            // sets up connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // sets request method to GET
            connection.setRequestMethod("GET");

            // connect to our API
            connection.connect();
            return connection;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // couldn't make connection
        return null;
    }
    public static String getCurrentTime() {

        // give current date and time
        LocalDateTime localDateTime = LocalDateTime.now();

        // format date and time into API congruent manner
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        // formatted string of current time
        String formattedDateTime = localDateTime.format(dateTimeFormatter);

        // return formatted string of current date and time
        return formattedDateTime;
    }
    public static int getIndexOfCurrentTime(JSONArray timeList) {

        // value for current time in the API congruent manner using our method
        String currentTime = getCurrentTime();

        // loop through our JSON Array to find time closest to our current time
        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                // return the corresponding index of that time
                return i;
            }
        }
        return 0;
    }
    private static String getWeatherCode(long weatherCode) {

        // modifies behaviour of weatherCondition based upon the weatherCode value
        String weatherCondition = "";
        if (weatherCode == 0L) {
            weatherCondition = "Clear";
        } else if (weatherCode > 0L && weatherCode <= 3L) {
            weatherCondition = "Cloudy";
        } else if ((weatherCode <= 67 && weatherCode >= 51) || weatherCode >= 80L && weatherCode <= 99L) {
            weatherCondition = "Rain";
        } else if (weatherCode >= 71 && weatherCode <= 77) {
            weatherCondition = "Snow";
        }
        return weatherCondition;
    }
}
