package weather_Application;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp extends JFrame {
	private static final String API_KEY = "7001f2982f399bc6803db5609d8e7b24";
	private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=7001f2982f399bc6803db5609d8e7b24";

	private JTextField locationTextField;
	private JTextPane resultTextPane;

	public WeatherApp() {
		setTitle("Weather App");   
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); 

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel inputPanel = new JPanel(new BorderLayout());
		JLabel locationLabel = new JLabel("Enter a location:");
		locationTextField = new JTextField();
		locationTextField.setPreferredSize(new Dimension(200, 30));
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchWeather();
			}
		});

		inputPanel.add(locationLabel, BorderLayout.WEST);
		inputPanel.add(locationTextField, BorderLayout.CENTER);
		inputPanel.add(searchButton, BorderLayout.EAST);

		resultTextPane = new JTextPane();
		resultTextPane.setEditable(false);
		resultTextPane.setContentType("text/html");

		mainPanel.add(inputPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(resultTextPane), BorderLayout.CENTER);

		add(mainPanel);
		setVisible(true);
	}

	private void searchWeather() {
		String location = locationTextField.getText().trim();
		if (!location.isEmpty()) {
			try {
				String url = String.format(API_URL, location);
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("GET");

				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder result = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				rd.close();

				displayWeatherData("<html><body><h2 style=\"color: #0066cc;\">Weather data for " + location
						+ ":</h2><p>" + result.toString() + "</p></body></html>");
			} catch (Exception ex) {
				displayErrorMessage("Failed to retrieve weather data for " + location
						+ ". Please check your input or try again later.");
			}
		} else {
			displayErrorMessage("Please enter a location.");
		}
	}

	private void displayWeatherData(String message) {
		resultTextPane.setText(message);
	}

	private void displayErrorMessage(String message) {
		resultTextPane.setContentType("text/html");
		resultTextPane.setText("<html><body><p style=\"color: red;\"><b>Error:</b> " + message + "</p></body></html>");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new WeatherApp();
			}
		});
	}
}   
