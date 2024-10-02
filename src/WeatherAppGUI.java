import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGUI extends JFrame {

    private JSONObject weatherData;

    // constructor for the GUI class
    public WeatherAppGUI() {
        super("Weather App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        addUiComponents();
        setVisible(true);
        getContentPane().setBackground(Color.GRAY);
    }

    // adds all the components of the ui to the JFrame
    public void addUiComponents() {

        // create a search text box using JTextField
        JTextField searchField = new JTextField();
        searchField.setBounds(15, 15, 351, 45);
        searchField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchField);

        // creates the default weather image when the app is loaded using a JLabel
        JLabel weatherImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherImage.setBounds(0,125,450,217);
        weatherImage.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherImage);

        // creates the text in Degrees Celsius for the temperature heading of the UI using a JLabel
        JLabel temperatureText = new JLabel("10 C°");
        temperatureText.setFont(new Font("Monospaced", Font.BOLD, 34));
        temperatureText.setBounds(0,335,450,30);
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        // creates the temperature description text to match according to what the temperature is, using a JLabel
        JLabel temperatureDescription = new JLabel("Cloudy");
        temperatureDescription.setBounds(0,370,440,36);
        temperatureDescription.setFont(new Font("Monospaced", Font.BOLD, 32));
        temperatureDescription.setHorizontalAlignment(SwingConstants.CENTER);
        temperatureDescription.setForeground(Color.WHITE);
        add(temperatureDescription);

        // creates the Humidity image using a JLabel
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(10,500,74,66);
        add(humidityImage);

        // creates the text for Humidity using a JLabel
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        humidityText.setForeground(Color.CYAN);
        humidityText.setBounds(90,500,85,55);
        add(humidityText);

        // creates the image for Wind Speed using a JLabel
        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220,500,80,66);
        add(windspeedImage);

        // creates the text for Wind Speed using a JLabel
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 9mp/h</html>");
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        windspeedText.setBounds(310,500,100,55);
        windspeedText.setForeground(Color.getHSBColor(100,200,300));
        add(windspeedText);

        // JButton created as a search button when user inputs location
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        // setCursor method used so when the cursor hovers over the button it becomes a hand cursor
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Linking the frontend to the backend using an ActionListener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // allows the user to type text in the search field
                String userInput = searchField.getText();

                // removes whitespaces so empty searches can't be made
                if (userInput.replaceAll("\\s","").length() <= 0) {
                    return;
                }

                // initialises weatherData to the JSON object with finalised data in App Class
                weatherData = WeatherApplication.getWeatherData(userInput);

                // switch case block for changing the image depending on weather condition
                String updateWeatherCondition = (String) weatherData.get("weather_condition");
                switch (updateWeatherCondition) {
                    case "Clear":
                        weatherImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        weatherImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }

                // Outputs the temperature data to be displayed by the UI
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + "C");

                // Outputs humidity data for display on the UI
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b>" + " " + humidity + "%" + "</html>");

                // Outputs the weather condition description for display
                temperatureDescription.setText(updateWeatherCondition);

                // Outputs Wind Speed data for display
                double windspeed = (double) weatherData.get("windspeed");
                windspeedText.setText("<html><b>Windspeed</b>" + " " + windspeed + "mp/h" +"</html>");
            }
        });
        // setting location of the search button and adding it to the JFrame
        searchButton.setBounds(375,13,47,45);
        add(searchButton);
    }

    // image method returning an ImageIcon to add them to their corresponding Swing Objects.
    private ImageIcon loadImage(String path) {

        try {

            // reads the image's file path (image data)
            BufferedImage image = ImageIO.read(new File(path));

            // returns as a new ImageIcon which contains the buffered Image
            return new ImageIcon(image);
        }
        // runs in case the try code has any exceptions
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("System could not load image");
        }
        return null;
    }
}
