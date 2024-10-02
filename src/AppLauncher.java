import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {

        // Allows thread safe behaviours for the GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Instantiated Weather App
                new WeatherAppGUI();
            }
        });
    }
}
