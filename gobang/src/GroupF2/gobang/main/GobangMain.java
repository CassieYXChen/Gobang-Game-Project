package GroupF2.gobang.main;

import GroupF2.gobang.base.Information;
import GroupF2.gobang.login.UserLogin;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GobangMain extends Application {
	// Default IP address for fallback
	private static final String DEFAULT_IP = "127.0.0.1";
	// Prefix to identify valid IP addresses within a specific range
	private static final String IP_PREFIX = "10.13.";

	// Main method to launch the JavaFX application
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	// Start method to initialize the JavaFX stage
	public void start(Stage stage) throws Exception {
		setupApplication(stage);
	}

	// Sets up the initial stage and global configurations for the application
	private void setupApplication(Stage stage) {
		String hostIP = fetchHostIP();
		Information.myIP = hostIP; // Update the global IP address variable

		showLoginScreen(stage);
	}

	// Displays the login screen to the user
	private void showLoginScreen(Stage stage) {
		UserLogin userLogin = new UserLogin();
		UserLogin.stage = stage; // Assign the stage to the UserLogin class
		userLogin.show(); // Display the login screen
	}

	// Fetches the host IP address by checking available network interfaces
	public static String fetchHostIP() {
		try {
			Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
			String ip = findSuitableIPAddress(networks);
			return ip != null ? ip : DEFAULT_IP; // Return the found IP or default if none found
		} catch (SocketException e) {
			System.err.println("Error retrieving network interfaces: " + e.getMessage());
			return DEFAULT_IP;
		}
	}

	// Iterates through network interfaces to find a suitable IP address
	private static String findSuitableIPAddress(Enumeration<NetworkInterface> networks) {
		while (networks.hasMoreElements()) {
			NetworkInterface network = networks.nextElement();
			Enumeration<InetAddress> addresses = network.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress address = addresses.nextElement();
				if (isValidIPAddress(address)) {
					return address.getHostAddress(); // Return the valid IP address
				}
			}
		}
		return null; // Return null if no valid IP found
	}

	// Checks if an IP address is valid based on defined criteria
	private static boolean isValidIPAddress(InetAddress address) {
		return !address.isLoopbackAddress() && // Not a loopback address
				address.getAddress().length == 4 && // IPv4 address
				address.getHostAddress().startsWith(IP_PREFIX); // Starts with the defined IP prefix
	}
}
