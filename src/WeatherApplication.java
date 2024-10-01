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
            HttpURLConnection connection = fetchAPIResponse(locationName);

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
    private static String getCurrentTime() {
        LocalDateTime localDateTime = LocalDateTime.now();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedDateTime = localDateTime.format(dateTimeFormatter);
        return formattedDateTime;
    }
    private static JSONObject indexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();
        

    }
}
