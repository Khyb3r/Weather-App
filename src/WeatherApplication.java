import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApplication {
    public static JSONArray getLocationData(String locationName) {
        locationName = locationName.replace(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="
                + locationName+ "&count=10&language=en&format=json";

        try {
            HttpURLConnection conn = fetchAPIResponse(urlString);

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("Error, HttpResponseCode: " + responseCode);
            }
            else{
                StringBuilder stringBuilder = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                while(scanner.hasNext()) {
                    stringBuilder.append(scanner.nextLine());
                }
                scanner.close();

                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject jsonObjectResults = (JSONObject) parser.parse(String.valueOf(stringBuilder));

                JSONArray locationData = (JSONArray) jsonObjectResults.get("results");
                return locationData;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static JSONObject getWeatherData(String locationName) {
        JSONArray locationData = getLocationData(locationName);
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitude+"&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";

        try {
            HttpURLConnection connection = fetchAPIResponse(urlString);

            if (connection.getResponseCode()!= 200) {
                throw new RuntimeException("Error, HttpResponseCode: " + connection.getResponseCode());
            }
            StringBuilder stringBuilder = new StringBuilder();

            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                stringBuilder.append(scanner.nextLine());
            }

            scanner.close();

            connection.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject weatherJsonResults = (JSONObject) parser.parse(String.valueOf(stringBuilder));
            JSONObject hourlyWeatherData = (JSONObject) weatherJsonResults.get("hourly");

            JSONArray timeArray = (JSONArray) hourlyWeatherData.get("time");
            int index = getIndexOfCurrentTime(timeArray);

            JSONArray temperatureData = (JSONArray) hourlyWeatherData.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            JSONArray weatherCode = (JSONArray) hourlyWeatherData.get("weather_code");
            String weatherCondition = getWeatherCode((long)weatherCode.get(index));

            JSONArray humidityData = (JSONArray) hourlyWeatherData.get("relative_humidity_2m");
            long humidity = (long) humidityData.get(index);

            JSONArray windspeedData = (JSONArray) hourlyWeatherData.get("wind_speed_10m");
            double windspeed = (double) windspeedData.get(index);

            JSONObject weatherData = new JSONObject();
            weatherData.put("windspeed", windspeed);
            weatherData.put("humidity", humidity);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("temperature", temperature);

            return weatherData;
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
    private static HttpURLConnection fetchAPIResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();
            return connection;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getCurrentTime() {
        LocalDateTime localDateTime = LocalDateTime.now();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedDateTime = localDateTime.format(dateTimeFormatter);
        return formattedDateTime;
    }
    public static int getIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                return i;
            }
        }
        return 0;
    }
    private static String getWeatherCode(long weatherCode) {
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
