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

    public void addUiComponents() {
        JTextField searchField = new JTextField();
        searchField.setBounds(15, 15, 351, 45);
        searchField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchField);

        JLabel weatherImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherImage.setBounds(0,125,450,217);
        weatherImage.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherImage);

        JLabel temperatureText = new JLabel("10 C°");
        temperatureText.setFont(new Font("Monospaced", Font.BOLD, 34));
        temperatureText.setBounds(0,335,450,30);
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        JLabel temperatureDescription = new JLabel("Cloudy");
        temperatureDescription.setBounds(0,370,440,36);
        temperatureDescription.setFont(new Font("Monospaced", Font.BOLD, 32));
        temperatureDescription.setHorizontalAlignment(SwingConstants.CENTER);
        temperatureDescription.setForeground(Color.WHITE);
        add(temperatureDescription);

        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(10,500,74,66);
        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        humidityText.setForeground(Color.CYAN);
        humidityText.setBounds(90,500,85,55);

        add(humidityText);

        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220,500,80,66);
        add(windspeedImage);

        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 9mp/h</html>");
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        windspeedText.setBounds(310,500,100,55);
        windspeedText.setForeground(Color.getHSBColor(100,200,300));
        add(windspeedText);

        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchField.getText();

                if (userInput.replaceAll("\\s","").length() <= 0) {
                    return;
                }

                weatherData = WeatherApplication.getWeatherData(userInput);

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

                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + "C");

                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b>" + " " + humidity + "%" + "</html>");

                temperatureDescription.setText(updateWeatherCondition);

                double windspeed = (double) weatherData.get("windspeed");
                windspeedText.setText("<html><b>Windspeed</b>" + " " + windspeed + "mp/h" +"</html>");
            }
        });
        searchButton.setBounds(375,13,47,45);
        add(searchButton);

    }

    private ImageIcon loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));

            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("System could not load image");
        return null;
    }
}
