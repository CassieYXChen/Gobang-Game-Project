package GroupF2.gobang.base;

import GroupF2.gobang.message.Message;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * Utility class for sending network messages
 * Author: CUHK CSCI3100 2023-24 Group F2
 */
public class NetMessage {
	/**
	 * Client sends message to server
	 * @param message The message to be sent
	 * @param oppoIP The IP address of the opponent
	 */
	public static void sendMessage(Message message, String oppoIP) {
		try (Socket socket = new Socket(oppoIP, Information.oppoPort);
			 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

			oos.writeObject(message);

		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR, "There was an error connecting to the opponent! Please try again later!");
			alert.setTitle("Error");
			alert.setHeaderText("Error: ");
			alert.getButtonTypes().setAll(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
			alert.showAndWait();
		}
	}
}
