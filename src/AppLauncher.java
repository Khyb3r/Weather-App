import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              //  new WeatherAppGUI().setVisible(true);
              //  System.out.println(WeatherApplication.getLocationData("Tokyo"));
                System.out.println(WeatherApplication.getWeatherData("Tokyo"));
            }
        });
    }
}
