package GroupF2.gobang.login;

import GroupF2.gobang.chessboard.ChessBoard;
import GroupF2.gobang.base.Information;
import GroupF2.gobang.base.EncryptionMD5;
import GroupF2.gobang.base.MessageAcceptor;
import GroupF2.gobang.serviceDao.AddressServiceDaoImpl;
import GroupF2.gobang.serviceDao.SinfoServiceDaoImpl;
import GroupF2.gobang.serviceDao.UserServiceDaoImpl;
import GroupF2.gobang.pojo.Address;
import GroupF2.gobang.pojo.Sinfo;
import GroupF2.gobang.pojo.User;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * User Login
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class UserLogin extends Stage{
	// Main stage
	public static Stage stage;
	// Database control
	private UserServiceDaoImpl userServiceDao = new UserServiceDaoImpl();
	private AddressServiceDaoImpl addressServiceDao = new AddressServiceDaoImpl();
	private SinfoServiceDaoImpl sinfoServiceDao = new SinfoServiceDaoImpl();
	// Input account
	private TextField account;
	// Input password
	private PasswordField  passwordField;
	// Remember password option
	CheckBox check;

	public UserLogin() {
		setupStage();
		Pane pane = (Pane) this.getScene().getRoot();
		addText(pane);
		addTextBox(pane);
		rememberPassword(pane);
		rememberAccount();
		isRememberPassword();
		addButton(pane);
	}

	private void setupStage() {
		this.setResizable(false);
		this.getIcons().add(new Image("images/logo.jpg"));
		this.setTitle("Login");
		Pane pane = new Pane();
		pane.setBackground(new Background(new BackgroundFill(Color.rgb(144, 202, 196), null, null)));
		this.setScene(new Scene(pane, 400, 300));
	}

	private void addText(Pane pane) {
		String[] labels = {"Account: ", "Password: "};
		for (int i = 0; i < labels.length; i++) {
			Text text = new Text(40, 50 + i * 70, labels[i]);
			text.setFill(Color.BLACK);
			text.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 15));
			pane.getChildren().add(text);
		}
	}

	private void addTextBox(Pane pane) {
		account = createTextField(33);
		passwordField = createPasswordField(103);
		pane.getChildren().addAll(account, passwordField);
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
	 * Remember password function
	 */
	public void rememberPassword(Pane pane) {
		// Add remember password option
		check = new CheckBox();
		check.setLayoutX(125);
		check.setLayoutY(150);
		Text text = new Text(160, 166,"Remember Password");
		text.setFill(Color.BLACK);
		text.setFont(Font.font("Times New Roman",
				FontPosture.REGULAR, 14));
		pane.getChildren().addAll(check, text);
	}
	/**
	 * Remember password realization
	 */
	public void isRememberPassword() {
		Address address = addressServiceDao.queryAccountByIP(Information.myIP);
		// Get account and IP address from user address database
		if (address != null) {
			boolean isRemember = address.getRemember() == 1 ? true : false;
			check.setSelected(isRemember);
			// If "Remember Password" is chosen
			if (isRemember) {
				// Remember password
				passwordField.setText(userServiceDao.queryUserByAccount(address.getAccount()).getPassword());
			}
		}
	}
	/**
	 * Remember Account realization
	 */
	public void rememberAccount() {
		if (Information.myIP != null) {
			// Search account through IP
			Address address = addressServiceDao.queryAccountByIP(Information.myIP);
			// If the account is already in the database, fill this account into the blank
			if (address != null) {
				this.account.setText(address.getAccount());
			}
		}
	}

	/**
	 * Add Login/Cancel/Register buttons
	 */
	private void addButton(Pane pane) {
		// Login button
		this.loginButton(pane);

		// Cancel button
		this.cancelButton(pane);

		// Register button
		this.registerButton(pane);
	}
	/**
	 * Login realization
	 */
	private void loginButton(Pane pane) {
		Button oKButton = new Button("Confirm");
		oKButton.setPrefSize(80, 40);
		oKButton.setLayoutX(60);
		oKButton.setLayoutY(200);
		// Connect buttons with "return" button of the keyboard
		oKButton.setDefaultButton(true);
		pane.getChildren().add(oKButton);
		// Click button to show IP settlement screen
		oKButton.setOnAction(e -> {
			// Login code
			login(pane);
		});
	}
	/**
	 * Progress after clicking login
	 */
	private void login(Pane pane) {
		if ("".equals(account.getText()) || "".equals(passwordField.getText())) {
			showAlert("Account or password should not be empty!");
			return;
		}

		User user = userServiceDao.queryUserByAccountAndPassword(account.getText(), passwordField.getText());
		if (user == null) {
			String md5Password = EncryptionMD5.digest32(passwordField.getText());
			user = userServiceDao.queryUserByAccountAndPassword(account.getText(), md5Password);
			if (user == null) {
				showAlert("Account or password is wrong!");
				return;
			}
		}

		Sinfo sinfo = sinfoServiceDao.queryIPByAccount(account.getText());
		if (sinfo.getStatus() != 0) {
			showAlert("This account is already online, so you cannot login");
			return;
		}

		Information.account = account.getText();
		Address address = addressServiceDao.queryAccountByIP(Information.myIP);
		if (address == null) {
			address = new Address();
			address.setAccount(Information.account);
			address.setAddress(Information.myIP);
			addressServiceDao.saveAddress(address);
		} else if (!Information.account.equals(address.getAccount())) {
			addressServiceDao.updateAccount(Information.myIP, Information.account);
		}

		addressServiceDao.updateRemember(check.isSelected() ? 1 : 0, Information.account);

		Sinfo queryIPByAccount = sinfoServiceDao.queryIPByAccount(Information.account);
		if (!queryIPByAccount.getAddress().equals(Information.myIP)) {
			sinfoServiceDao.updateIPByAccount(Information.account, Information.myIP);
		}
		sinfoServiceDao.updateStatusByAccount(Information.account, 1);

		this.close();
		this.stage.close();

		MessageAcceptor messageAcceptor = new MessageAcceptor();
		Thread boardThread = new Thread(messageAcceptor);
		boardThread.start();

		ChessBoard chessBoard = new ChessBoard();
		chessBoard.show();
	}

	private void showAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message);
		alert.initOwner(this);
		alert.setTitle("Warning");
		alert.setHeaderText("Warning: ");
		ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(confirmButton);
		alert.show();
	}

	/**
	 * The "Cancel" button program
	 */
	private void cancelButton(Pane pane) {
		Button cancelButton = new Button("Cancel");
		cancelButton.setPrefSize(80, 40);
		cancelButton.setLayoutX(160);
		cancelButton.setLayoutY(200);
		// Connect the "Cancel" button with "Esc" of the keyboard
		cancelButton.setCancelButton(true);
		pane.getChildren().add(cancelButton);

		// Process after clicking the "Cancel" button
		cancelButton.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure to quit the login page?",
					new ButtonType("No",  ButtonData.NO),
					new ButtonType("Yes",  ButtonData.YES));
			alert.initOwner(this);
			alert.setTitle("Cancel");
			alert.setHeaderText("Click Confirm will cancel the registration");
			Optional<ButtonType> button = alert.showAndWait();
			if (button.get().getButtonData() == ButtonData.YES) {
				// Close login page
				this.close();
			}
		});
	}
	/**
	 * Register button program
	 */
	private void registerButton(Pane pane) {
		Button registerButton = new Button("Register");
		registerButton.setPrefSize(80, 40);
		registerButton.setLayoutX(260);
		registerButton.setLayoutY(200);
		pane.getChildren().add(registerButton);
		// Click the buttone to show IP and settlement page
		registerButton.setOnAction(e -> {
			// Close login page
			this.close();
			UserRegister userRegister = new UserRegister();
			// Show register page
			userRegister.show();
		});
	}
}
