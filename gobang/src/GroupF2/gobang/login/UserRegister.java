package GroupF2.gobang.login;


import GroupF2.gobang.base.Information;
import GroupF2.gobang.base.JdbcConnection;
import GroupF2.gobang.base.EncryptionMD5;
import GroupF2.gobang.serviceDao.SinfoServiceDaoImpl;
import GroupF2.gobang.serviceDao.UserServiceDaoImpl;
import GroupF2.gobang.pojo.Sinfo;
import GroupF2.gobang.pojo.User;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * User Register
 */
public class UserRegister extends Stage{
	// Manage database
	private UserServiceDaoImpl userServiceDao = new UserServiceDaoImpl();
	private SinfoServiceDaoImpl sinfoServiceDao = new SinfoServiceDaoImpl();
	// Input account
	private TextField account;
	// Input password
	private PasswordField  password;
	// Input password again to confirm
	private PasswordField confirmPassword;

	public UserRegister () {
		setupStage();
		Pane pane = (Pane) this.getScene().getRoot();
		addText(pane);
		addTextBox(pane);
		addButton(pane);
	}

	private void setupStage() {
		// Fix screen size
		this.setResizable(false);
		// Set logo
		this.getIcons().add(new Image("images/logo.jpg"));
		// Set page title
		this.setTitle("Register");
		// Create main stage
		Pane pane = new Pane();
		// Set background
		pane.setBackground(new Background(new BackgroundFill(Color.rgb(144, 202, 196),null,null)));
		// Create scene object
		Scene scene = new Scene(pane, 400, 300);
		// Put the scene into stage
		this.setScene(scene);
	}

	/**
	 * Account, password and confirmed password text
	 */
	private void addText(Pane pane) {
		String[] labels = {"Account: ", "Password: ", "Confirm password: "};
		for (int i = 0; i < labels.length; i++) {
			Text text = new Text(10, 50 + i * 70, labels[i]);
			text.setFill(Color.BLACK);
			text.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 15));
			pane.getChildren().add(text);
		}
	}

	/**
	 * Account, password, confirmed password textbox
	 */
	private void addTextBox(Pane pane) {
		account = createTextField(33);
		password = createPasswordField(103);
		confirmPassword = createPasswordField(173);
		pane.getChildren().addAll(account, password, confirmPassword);
	}
	private TextField createTextField(int layoutY) {
		TextField textField = new TextField();
		textField.setPrefSize(150, 15);
		textField.setLayoutX(130);
		textField.setLayoutY(layoutY);
		return textField;
	}

	private PasswordField createPasswordField(int layoutY) {
		PasswordField passwordField = new PasswordField();
		passwordField.setPrefSize(150, 15);
		passwordField.setLayoutX(130);
		passwordField.setLayoutY(layoutY);
		return passwordField;
	}
	/**
	 * Confirm and cancel buttons
	 */
	private void addButton(Pane pane) {
		// Confirm button
		this.registerButton(pane);

		// Cancel button
		this.cancelButton(pane);

	}
	/**
	 * Confirm password program
	 */
	private void registerButton(Pane pane) {
		Button registerButton = new Button("Register");
		registerButton.setPrefSize(80, 40);
		registerButton.setLayoutX(100);
		registerButton.setLayoutY(240);
		// Connect button with "return" of the keyboard
		registerButton.setDefaultButton(true);
		pane.getChildren().add(registerButton);
		// Click button for user registration
		registerButton.setOnAction(e -> {
			this.register(pane);
		});
	}
	/**
	 * Click to register
	 */
	private void register(Pane pane) {
		String accountString = account.getText();
		String passwordString = password.getText();
		String confirmPasswordString = confirmPassword.getText();

		// Check input validity
		if ("".equals(accountString) || "".equals(passwordString) || "".equals(confirmPasswordString)) {
			showAlert("Account or password should not be empty!");
			return;
		}

		// Check password consistency
		if (!passwordString.equals(confirmPasswordString)) {
			showAlert("Passwords do not match!");
			return;
		}

		// Check account and password format
		if (!Pattern.matches("[a-zA-Z0-9_]{1,15}", accountString)) {
			showAlert("Account name should include 1-15 digits of English letters, numbers and underscores");
			return;
		}
		if (!Pattern.matches("[a-zA-Z0-9_]{6,15}", passwordString)) {
			showAlert("Password should include 6-15 digits of English letters, numbers and underscores");
			return;
		}

		// Check if account already exists
		if (userServiceDao.queryUserByAccount(accountString) != null) {
			showAlert("Account already exists!");
			return;
		}

		// Save user information to database
		saveUserToDatabase(accountString, confirmPasswordString);

		// Show login page and close register page
		new UserLogin().show();
		this.close();
	}

	private void showAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message);
		alert.initOwner(this);
		alert.setTitle("Warning");
		alert.setHeaderText("Warning: ");
		alert.getButtonTypes().setAll(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
		alert.show();
	}

	private void saveUserToDatabase(String accountString, String confirmPasswordString) {
		User nowUser = new User();
		nowUser.setAccount(accountString);
		nowUser.setPassword(EncryptionMD5.digest32(confirmPasswordString));
		nowUser.setRegTime(new Timestamp(System.currentTimeMillis()));

		Sinfo sinfo = new Sinfo();
		sinfo.setAccount(accountString);
		sinfo.setAddress(Information.myIP);

		Connection conn = null;
		try {
			conn = JdbcConnection.getConnection();
			JdbcConnection.disableAutocommit(conn);
			userServiceDao.saveUser(nowUser);
			sinfoServiceDao.saveSinfo(sinfo);
			JdbcConnection.commit(conn);
		} catch (Exception e) {
			JdbcConnection.rollback(conn);
		} finally {
			if (conn != null) {
				JdbcConnection.close(conn);
			}
		}
	}


	/**
	 * "Cancel" button program
	 */
	private void cancelButton(Pane pane) {
		Button cancelButton = new Button("Cancel");
		cancelButton.setPrefSize(80, 40);
		cancelButton.setLayoutX(250);
		cancelButton.setLayoutY(240);
		// Connect "Cancel" button with "Esc" of keyboard
		cancelButton.setCancelButton(true);
		pane.getChildren().add(cancelButton);

		// Click button to cancel connection
		cancelButton.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure to quit the registration page?",
					new ButtonType("No",  ButtonBar.ButtonData.NO),
					new ButtonType("Yes",  ButtonBar.ButtonData.YES));
			alert.initOwner(this);
			alert.setTitle("Cancel");
			alert.setHeaderText("Click Confirm will cancel the registration");
			alert.setContentText("Are you sure to cancel? ");
			Optional<ButtonType> button = alert.showAndWait();
			if (button.get().getButtonData() == ButtonBar.ButtonData.YES) {
				// Close login page
				this.close();
			}
		});
	}

}
