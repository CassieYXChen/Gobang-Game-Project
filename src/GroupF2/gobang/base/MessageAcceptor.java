package GroupF2.gobang.base;

import GroupF2.gobang.message.Message;
import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

// Accept messages sent by the client
public class MessageAcceptor implements Runnable {
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			// Create a ServerSocket for the server, specifying its port number
			serverSocket = new ServerSocket(Information.myPort);
		} catch (Exception e) {
			e.printStackTrace();
			// Terminate the thread if an exception occurs
			return;
		}
		// Continuously listen for messages from the client
		while (true) {
			try (Socket socket = serverSocket.accept()) {
				// Get the client's input stream
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				// Read the message object from the stream
				Message message = (Message)ois.readObject();
				// Process the message, passing it to the upDateUI method in the ChessBoard class
				Platform.runLater(() -> Information.chessBoard.upDateUI(message));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

