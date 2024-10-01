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
    }

    public void addUiComponents() {
        JTextField searchField = new JTextField();
        searchField.setBounds(15, 15, 351, 45);
        searchField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchField);


        JLabel weatherImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherImage.setBounds(0,125,450,217);
        add(weatherImage);

        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 24));
        temperatureText.setBounds(0,350,450,54);

        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        JLabel temperatureDescription = new JLabel("Cloudy");
        temperatureDescription.setBounds(0,405,450,36);
        temperatureDescription.setFont(new Font("Dialog", Font.PLAIN, 32));
        temperatureDescription.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureDescription);

        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(10,500,74,66);
        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        humidityText.setBounds(90,500,85,55);
        add(humidityText);

        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220,500,74,66);
        add(windspeedImage);

        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 9mp/h</html>");
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        windspeedText.setBounds(310,500,85,55);
        add(windspeedText);

        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchField.getText();

                if (userInput.replaceAll())

                weatherData = WeatherApplication.getWeatherData()

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
