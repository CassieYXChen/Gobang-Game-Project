package GroupF2.gobang.chessboard;

import GroupF2.gobang.base.*;
import GroupF2.gobang.message.*;
import GroupF2.gobang.pojo.Record;
import GroupF2.gobang.pojo.Sinfo;
import GroupF2.gobang.pojo.User;
import GroupF2.gobang.record.MyRecord;
import GroupF2.gobang.record.OpponentRecord;
import GroupF2.gobang.aiplayer.AIPlay;
import GroupF2.gobang.serviceDao.RecordServiceDaoImpl;
import GroupF2.gobang.serviceDao.SinfoServiceDaoImpl;
import GroupF2.gobang.serviceDao.UserServiceDaoImpl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Register and log in to the battle version of the chessboard
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class ChessBoard extends Stage {
	// Database operations
	private UserServiceDaoImpl userServiceDao;
	private RecordServiceDaoImpl recordServiceDao;
	private SinfoServiceDaoImpl sinfoServiceDao;
	// Number of horizontal and vertical lines on the Gomoku board
	private static final int row_num = 20;
	// Spacing between the lines on the board
	private static final int spacing = 40;
	// Indicates whether it's black's turn to move, black goes first (true)
	private boolean isBlack;
	// Indicates whether the game is over, default is true
	private boolean gameOver;
	// Stores the information of the chess pieces
	private List<ChessClass> chessClassList;
	// Radius of the chess pieces
	private static final int chess_size = 19;
	// Used to record whether there is a chess piece on a specific position on the board, false means no, true means yes
	private boolean[][] arr;
	// Used to record the color of the chess pieces on the board, together with arr, it provides O(1) time complexity for checking if there is a chess piece and its color at a specific position
	private Color[][] colors;

	// Indicates whether a move can be played, default is false
	private boolean isPlay;

	// Indicates whether the forbidden move rule is enabled, default is false
	private boolean isForbiddenMove;

	// Color of the player's own chess pieces
	private Color color;
	// Color of the player's own chess pieces, 1 for black, 0 for white
	private int blackOrWhite;

	// Whether the user can request undo, default is true (a user can only request undo once)
	private boolean replay;
	// Main stage
	private Pane pane;

	// Display text for online players
	private Text matchText;

	// List of all online users' texts
	private List<Text> textList;

	// Previous page button for online players
	private Button prePage;

	// Next page button for online players
	private Button nextPageButton;

	// Text displayed after sending undo request, match request, or new game request
	private Text waitText;

	// Whether the player has sent a match request, default is false
	private boolean isSend;

	// Whether the player has accepted a match request, default is false
	private boolean isAccept;

	// Indicator for the last placed chess piece
	private Circle redCircle;

	// Display range for chat messages
	TextArea textArea;
	// Text field for sending messages
	TextField messageText;

	// Starting page for pagination, starting from 0
	private int index;

	// Number of players displayed per page in pagination
	private static final int user_num = 2;

	// Texts displayed on the board after the game starts
	private List<Text> startTextList;

	// Circle displayed behind the player's account after the game starts
	private Circle myAccountCircle;

	// Circle displayed behind the opponent's account after the game starts
	private Circle opponentAccountCircle;

	// Circle displayed after the current move text in the game starts
	private Circle currentChess;

	// Timeline class used for slideshow of stage background images
	private Timeline timeline1;
	private Timeline timeline2;

	// List of background images
	private List<ImageView> imageList;
	private List<MediaPlayer> mediaPlayerList;

	// Index for locating the current background image, default is 0
	private int imageIndex;

	// Index for locating the current background music, default is 0
	private int musicIndex;

	// Controls the pause and play of the slideshow buttons, default is pause
	private boolean playImages;
	private boolean playMusics;
	// Background music
	private MediaPlayer mediaPlayer;
	private LocalDateTime startTime;
	private Text startTimeText;
	// Game time text
	private Timeline timeUpdater; // Timer for updating time display
	private Text gameTimeText;

	// Game time countdown
	private Timeline gameTimeline;

	// Step time text
	private Text stepTimeText;

	// Step time countdown
	private Timeline stepTimeline;

	// Controls countdown for game time
	private int gameTimeNum;

	// Controls countdown for step time
	private int stepTimeNum;

	// Dropdown button
	private SplitMenuButton menuButton;

	// Controls the pause and play of the background music, default is play
	private boolean playMusic;

	// Button for pausing and playing background music
	private Button musicButton;

	// Number of times the AI can be asked for help, maximum of 3 times
	private int hintNum;

	public String winner = null;
	private String player1 = null;
	private String player2 = null;

	public ChessBoard() {
		// Initialize the chessboard when it is opened
		this.start();
	}

	/**
	 * When the chessboard is opened, initialize the chessboard and its nodes
	 */
	private void start() {
		// Initialize member variables
		this.initializeVariables();
		// Play background music
		mediaPlayer.play();
		// Loop playback
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		// Save the chessboard object to Global
		Information.chessBoard = this;

		// Override the method for closing the Gomoku window
		this.newCloseRequest();

		// Add background images to the collection
		this.addImages();

		this.addMusics();

		// Create the stage scene
		this.initializeStage();

		// Add timeline class to control the logic
		this.createTimeline();

		// Draw lines on the chessboard, add markings for the five key points, and display text
		this.addText();

		// Add text box
		this.addTextBox();

		// Generate chess pieces at corresponding positions on the chessboard when clicked by the mouse
		this.mouseClikedChessboard();

		// Add buttons
		this.addButton();
	}

	// Initialize member variables
	private void initializeVariables() {
		// Initialize services
		userServiceDao = new UserServiceDaoImpl();
		recordServiceDao = new RecordServiceDaoImpl();
		sinfoServiceDao = new SinfoServiceDaoImpl();

		// Initialize game-related variables
		isBlack = true; // Indicates black's turn to move
		gameOver = true; // Indicates if the game is over
		replay = true; // Enables undo request
		isForbiddenMove = false; // Indicates if the forbidden move rule is enabled
		hintNum = 3; // Number of times AI can be asked for help

		// Initialize data structures
		chessClassList = new ArrayList<>(); // Stores chess piece information
		arr = new boolean[row_num][row_num]; // Records presence of chess pieces on the board
		colors = new Color[row_num][row_num]; // Records colors of chess pieces

		textList = new ArrayList<>(); // List of online users' texts
		waitText = new Text(200, 300, ""); // Text displayed after requests
		redCircle = new Circle(4); // Indicator for last placed chess piece
		textArea = new TextArea(); // Display range for chat messages
		startTextList = new ArrayList<>(); // Texts displayed after game starts
		myAccountCircle = new Circle(1100, 72, chess_size); // Circle displayed behind player's account
		opponentAccountCircle = new Circle(1100, 212, chess_size); // Circle displayed behind opponent's account
		currentChess = new Circle(1100, 282, chess_size); // Circle displayed after current move text

		// Initialize multimedia-related variables
		imageList = new ArrayList<>(); // List of background images
		mediaPlayerList = new ArrayList<>(); // List of media players for background music

		// Initialize background music
		mediaPlayer = new MediaPlayer(new Media(getClass().getResource("/music/BGM1.mp3").toString()));
		playMusic = true; // Controls background music playback
	}

	/**
	 * Initialize the stage
	 */
	private void initializeStage() {
		// Set window size to be resizable
		this.setResizable(true);
		// Set title
		this.setTitle("Gobang");
		// Set icon
		this.getIcons().add(new Image("images/logo.jpg"));
		// Create main stage
		pane = new Pane();
		// Create scene object with width 1400 and height 900 pixels
		Scene scene = new Scene(pane, 1400, 900);
		// Set the background color of the multiline text box to transparent using a css file
		scene.getStylesheets().add(getClass().getResource("BackgroundColor.css").toExternalForm());
		// Add the scene to the stage
		this.setScene(scene);
		// Set initial background image
		pane.getChildren().add(new ImageView("images/image7.jpg"));
	}

	/**
	 * Logic of mouse clicks on the chessboard
	 */
	private void mouseClikedChessboard() {
		pane.setOnMouseClicked(e -> {
			if (!gameOver && isPlay) {
				double x = e.getX();
				double y = e.getY();
				if (x < 40 || x > 820 || y < 40 || y > 820) {
					return;
				}
				int xIndex = (int) Math.round((x - 50) / 40);
				int yIndex = (int) Math.round((y - 50) / 40);
				this.piece(xIndex, yIndex);
			}
		});
	}

	/**
	 * Overriding the method after closing the Gobang window
	 */
	private void newCloseRequest() {
		this.setOnCloseRequest(e -> {
			if (Information.temporaryOppoIP != null) {
				NetMessage.sendMessage(new EscapeMessage(), Information.temporaryOppoIP);
				if (!gameOver) {
					ResultMessage resultMessage = new ResultMessage();
					resultMessage.setAccount(Information.account);
					resultMessage.setResult(Color.BLACK.equals(this.color) ? ResultMessage.WHITE_WIN : ResultMessage.BLACK_WIN);
					NetMessage.sendMessage(resultMessage, Information.oppoIP);
				}
			}
			sinfoServiceDao.updateStatusByAccount(Information.account, 0);
			System.exit(0);
		});
	}

	/**
	 * Used to control the game time, step time countdown and chessboard background image rotation
	 */
	private void createTimeline() {
		startTime = LocalDateTime.now();
		this.changeImagesTimeline();
		this.changeMusicTimeline();
		this.controlGameTimeline();
		this.controlStepTimeline();
	}

	/**
	 * Control game time countdown
	 */
	private void controlGameTimeline() {
		this.startTimeText = new Text(30, 30, "Start time: " + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		startTimeText.setFill(Color.DARKBLUE);
		startTimeText.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 25));
		pane.getChildren().add(startTimeText);

		this.gameTimeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> gameTimeText()));
		gameTimeline.setCycleCount(Timeline.INDEFINITE);
		gameTimeline.play();
		gameTimeline.pause();

		this.gameTimeText = new Text(395, 30, "Elapsed time 00:00");
		gameTimeText.setFill(Color.DARKBLUE);
		gameTimeText.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 25));
		pane.getChildren().add(gameTimeText);

		this.gameTimeNum = 0;
	}

	/**
	 * Game time text, countdown logic
	 */
	private void gameTimeText() {
		if (gameTimeNum == 600) {
			gameTimeline.pause();
			NetMessage.sendMessage(new CountdownMessage(), Information.oppoIP);
			sendResultMessage();
			this.specialLose();
			return;
		}
		int minute = gameTimeNum / 60;
		int second = gameTimeNum % 60;
		this.gameTimeText.setText("Elapsed time " + (minute < 10 ? ("0" + minute) : minute) + ":" + (second < 10 ? ("0" + second) : second));
		gameTimeNum++;
	}

	/**
	 * Control step time countdown
	 */
	private void controlStepTimeline() {
		this.stepTimeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> stepTimeText()));
		stepTimeline.setCycleCount(Timeline.INDEFINITE);
		stepTimeline.play();
		stepTimeline.pause();

		this.stepTimeText = new Text(630, 30, "Move time limit 60");
		stepTimeText.setFill(Color.DARKBLUE);
		stepTimeText.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 25));
		pane.getChildren().add(stepTimeText);

		stepTimeNum = 60;
	}

	/**
	 * Step time text, countdown logic
	 */
	private void stepTimeText() {
		if (stepTimeNum == 0) {
			stepTimeline.pause();
			NetMessage.sendMessage(new CountdownMessage(), Information.oppoIP);

			sendResultMessage();
			this.specialLose();
			return;
		}
		this.stepTimeText.setText("Move time limit " + --stepTimeNum);
	}

	/**
	 * Send result message
	 */
	private void sendResultMessage() {
		ResultMessage resultMessage = new ResultMessage();
		resultMessage.setAccount(Information.account);
		resultMessage.setResult(Color.BLACK.equals(this.color) ? ResultMessage.WHITE_WIN : ResultMessage.BLACK_WIN);
		NetMessage.sendMessage(resultMessage, Information.oppoIP);
	}

	/**
	 * Control dynamic rotation of chessboard background images
	 */
	private void changeImagesTimeline() {
		// Background image dynamic carousel, once every second
		timeline1 = new Timeline(new KeyFrame(Duration.millis(1000), e -> changeImages()));
		timeline1.setCycleCount(Timeline.INDEFINITE);
		timeline1.play();
		timeline1.pause();
	}

	/**
	 * Chessboard background image rotation
	 */
	private void changeImages() {
		if (imageIndex == imageList.size()) {
			imageIndex = 0;
		}
		pane.getChildren().set(0, imageList.get(imageIndex++));
	}

	/**
	 * Add background images to the collection
	 */
	private void addImages() {
		for (int i = 1; i <= 8; i++) {
			imageList.add(new ImageView("images/image" + i + (i == 1 || i == 3 || i == 7 ? ".jpg" : ".png")));
		}
	}

	private void changeMusicTimeline() {
		//Music dynamic carousel, every 6 seconds.
		timeline2 = new Timeline(new KeyFrame(Duration.millis(6000), e -> changeMusics()));
		timeline2.setCycleCount(Timeline.INDEFINITE);
		timeline2.play();
		timeline2.pause();
	}

	/**
	 * Background music carousel
	 */
	private void changeMusics() {
		this.mediaPlayer.pause();
		if (musicIndex == mediaPlayerList.size()) {
			musicIndex = 0;
		}
		mediaPlayer = mediaPlayerList.get(musicIndex++);
		this.mediaPlayer.play();
	}

	/**
	 * Add background music to the collection
	 */
	private void addMusics() {
		for (int i = 1; i <= 4; i++) {
			mediaPlayerList.add(new MediaPlayer(new Media(getClass().getResource("/music/BGM" + i + ".mp3").toString())));
		}
	}

	/**
	 * Add text box for sending messages and displaying messages
	 */
	private void addTextBox() {
		messageText = new TextField();
		messageText.setPrefSize(150, 20);
		messageText.setLayoutX(950);
		messageText.setLayoutY(800);

		textArea.setLayoutX(950);
		textArea.setLayoutY(450);
		textArea.setFont(Font.font(15));
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setStyle("-fx-text-fill:red");
		textArea.setPrefRowCount(10);
		textArea.setPrefWidth(300);
		textArea.setPrefHeight(350);

		pane.getChildren().addAll(messageText, textArea);
	}

	/**
	 * Add text to the chessboard, draw lines, add nine-point markers
	 */
	private void addText() {
		this.drawLine();
		this.drawNinePoint();
		this.addPlay();
		this.queryAllAccountShowSinfo(index * user_num, user_num);
		this.waitText();
	}

	/**
	 * Request for battle, regret, new game prompt
	 */
	private void waitText() {
		this.waitText.setFill(Color.DARKBLUE);
		this.waitText.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 40));
		pane.getChildren().add(this.waitText);
	}

	/**
	 * Query all online player account names and display on the chessboard
	 *
	 * @param index Pagination query start index
	 * @param size  Number per page
	 */
	private void queryAllAccountShowSinfo(int index, int size) {
		List<Sinfo> list = sinfoServiceDao.queryAllByLimit(index, size);
		if (!textList.isEmpty()) {
			pane.getChildren().removeAll(this.textList);
			textList.clear();
		}
		this.showSinfo(list);
	}

	/**
	 * Display online player account names on the chessboard
	 *
	 * @param list All online players collection
	 */
	private void showSinfo(List<Sinfo> list) {
		int count = 0;
		for (Sinfo sinfo : list) {
			Text text = createPlayerText(sinfo, count++);
			this.textList.add(text);
			pane.getChildren().add(text);
			text.setOnMouseClicked(e -> handlePlayerClick(sinfo));
		}
	}

	/**
	 * Display online player text
	 */
	private void addPlay() {
		matchText = new Text(900, 70, "Click on an available player to request a match.");
		matchText.setFill(Color.DARKBLUE);
		matchText.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 25));
		pane.getChildren().add(matchText);
	}

	private Text createPlayerText(Sinfo sinfo, int count) {
		Text text = new Text(970, 160 + (count * 40), sinfo.getAccount() + " (" + (sinfo.getStatus() == 1 ? " Available " : " Busy ") + ")");
		text.setFill(Color.DARKBLUE);
		text.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 25));
		return text;
	}

	private void handlePlayerClick(Sinfo sinfo) {
		if (!gameOver) {
			System.out.println("The game has already started, unable to send a match request!");
			return;
		}
		Sinfo nowSinfo = sinfoServiceDao.queryIPByAccount(sinfo.getAccount());
		if (isSend) {
			s1Alert("The match request has already been sent, please wait patiently!");
			return;
		}
		if (nowSinfo.getStatus() == 2) {
			s1Alert(sinfo.getAccount() + " is engaged in an intense battle, please find a different opponent!");
			return;
		}
		if (nowSinfo.getStatus() == 0) {
			s1Alert(sinfo.getAccount() + " The player is offline, please find a different opponent!");
			return;
		}
		if (sinfo.getAccount().equals(Information.account)) {
			s1Alert("Cannot select oneself as an opponent.");
			return;
		}

		Information.oppoIP = sinfoServiceDao.queryIPByAccount(sinfo.getAccount()).getAddress();
		this.isSend = !isSend;
		GameRequestMeaasge gameRequestMeaasge = new GameRequestMeaasge();
		gameRequestMeaasge.setAccount(Information.account);
		gameRequestMeaasge.setRequestType(GameRequestMeaasge.GAME_REQUEST);
		this.waitText.setText("A match request has been sent to " + sinfo.getAccount() + ", please wait patiently...");

		NetMessage.sendMessage(gameRequestMeaasge, Information.oppoIP);
		mediaPlayer.play();
		musicButton.setText("Pause the music");
		playMusic = !playMusic;
	}

	private void s1Alert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message);
		alert.initOwner(this);
		alert.setTitle("Warning");
		alert.setHeaderText("Warning: ");
		ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(confirmButton);
		alert.show();
	}

	private void drawLine() {
		// Drawing grid lines on the chessboard
		for (int i = 0; i < row_num; i++) {
			// Calculate the offset for the current line
			int offset = 50 + spacing * i;

			// Create and configure a horizontal line from (50, offset) to (810, offset)
			Line horizontalLine = new Line(50, offset, 810, offset);
			horizontalLine.setStroke(Color.DARKBLUE); // Set the color of the horizontal line
			pane.getChildren().add(horizontalLine); // Add the horizontal line to the pane

			// Create and configure a vertical line from (offset, 50) to (offset, 810)
			Line verticalLine = new Line(offset, 50, offset, 810);
			verticalLine.setStroke(Color.DARKBLUE); // Set the color of the vertical line
			pane.getChildren().add(verticalLine); // Add the vertical line to the pane
		}
	}

	private void drawNinePoint() {
		Circle circle1 = new Circle(170, 170, 4);
		circle1.setFill(Color.DARKBLUE);
		Circle circle2 = new Circle(410, 170, 4);
		circle2.setFill(Color.DARKBLUE);
		Circle circle3 = new Circle(650, 170, 4);
		circle3.setFill(Color.DARKBLUE);
		Circle circle4 = new Circle(170, 410, 4);
		circle4.setFill(Color.DARKBLUE);
		Circle circle5 = new Circle(410, 410, 4);
		circle5.setFill(Color.DARKBLUE);
		Circle circle6 = new Circle(650, 410, 4);
		circle6.setFill(Color.DARKBLUE);
		Circle circle7 = new Circle(170, 650, 4);
		circle7.setFill(Color.DARKBLUE);
		Circle circle8 = new Circle(410, 650, 4);
		circle8.setFill(Color.DARKBLUE);
		Circle circle9 = new Circle(650, 650, 4);
		circle9.setFill(Color.DARKBLUE);
		pane.getChildren().addAll(circle1, circle2, circle3, circle4, circle5, circle6, circle7, circle8, circle9);
	}

	/**
	 * Display opponent's move on your chessboard
	 *
	 * @param message
	 */
	private void chessMessage(Message message) {
		this.soundMoveLater();
		this.stopThread();
		this.gameTimeline.play();
		this.stepTimeline.play();
		if (!chessClassList.isEmpty()) {
			pane.getChildren().remove(redCircle);
		}
		currentChess.setFill(isBlack ? Color.ALICEBLUE : Color.BLACK);
		this.isPlay = true;
		ChessMessage chessMessage = (ChessMessage) message;
		int x = chessMessage.getX();
		int y = chessMessage.getY();
		Color nowColor = chessMessage.getBlackOrWhite() == 1 ? Color.BLACK : Color.ALICEBLUE;
		Circle circle = new Circle(x * 40 + 50, y * 40 + 50, chess_size);
		circle.setFill(nowColor);
		if (chessMessage.getBlackOrWhite() == 1) {
			this.blackOrWhite = 0;
			this.color = Color.ALICEBLUE;
		}
		isBlack = !isBlack;
		arr[x][y] = true;
		colors[x][y] = nowColor;
		ChessClass chessClass = new ChessClass(x, y, nowColor);
		chessClassList.add(chessClass);
		pane.getChildren().add(circle);
		redCircle.setCenterX(x * 40 + 50);
		redCircle.setCenterY(y * 40 + 50);
		redCircle.setFill(Color.RED);
		pane.getChildren().add(redCircle);
		if (this.isWin(chessClass)) {
			gameTimeline.pause();
			stepTimeline.pause();
			Information.oppoIP = null;
			this.removeEndTextAndCircle();
			this.gameOverReminder("Game lost");
		}
		if (chessClassList.size() == row_num * row_num) {
			this.chessFullReminder();
		}
	}

	/**
	 * Handle undo move message
	 *
	 * @param message
	 */
	private void replayMessage(Message message) {
		ReplayMessage replayMessage = (ReplayMessage) message;
		// Undo move request
		if (replayMessage.getFlag() == ReplayMessage.REPLAY_REQUEST) {
			Alert alert = createAlert("The opponent requests to undo the move. Do you agree?");
			Optional<ButtonType> button = alert.showAndWait();
			if (button.get().getButtonData() == ButtonData.YES) {
				pauseTimelines();
				resetStepTime();
				replayMessage.setFlag(ReplayMessage.REPLAY_AGRRE);
				undoMove();
			} else {
				replayMessage.setFlag(ReplayMessage.REPLAY_REFUSE);
			}
			NetMessage.sendMessage(replayMessage, Information.oppoIP);
			// Opponent agrees to undo move
		} else if (replayMessage.getFlag() == ReplayMessage.REPLAY_AGRRE) {
			startTimelines();
			this.waitText.setText("");
			replay = false;
			undoMove();
			// Opponent refuses to undo move
		} else if (replayMessage.getFlag() == ReplayMessage.REPLAY_REFUSE) {
			waitText.setText("");
			s2Alert("The opponent does not agree to undo the move!");
		}
	}

	private Alert createAlert(String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION, message,
				new ButtonType("NO", ButtonData.NO),
				new ButtonType("YES", ButtonData.YES));
		alert.initOwner(this);
		alert.setTitle("Request");
		alert.setHeaderText("Request: ");
		return alert;
	}

	private void pauseTimelines() {
		gameTimeline.pause();
		stepTimeline.pause();
	}

	private void resetStepTime() {
		this.stepTimeText.setText("Move time limit 60");
		this.stepTimeNum = 60;
	}

	private void undoMove() {
		int lastIndex = chessClassList.size() - 1;
		ChessClass chessClass = chessClassList.get(lastIndex);
		arr[chessClass.getX()][chessClass.getY()] = false;
		colors[chessClass.getX()][chessClass.getY()] = null;
		if (chessClassList.size() >= 1) {
			isBlack = !isBlack;
			isPlay = !isPlay;
			currentChess.setFill(isBlack ? Color.BLACK : Color.ALICEBLUE);
		}
		pane.getChildren().remove(redCircle);
		removeLastPiece();
		chessClassList.remove(lastIndex);
		if (!chessClassList.isEmpty()) {
			addRedCircleToLastPiece();
		}
	}

	private void removeLastPiece() {
		for (int i = pane.getChildren().size() - 1; i >= 0; i--) {
			if (pane.getChildren().get(i) instanceof Circle) {
				pane.getChildren().remove(i);
				break;
			}
		}
	}

	private void addRedCircleToLastPiece() {
		ChessClass chessClass1 = chessClassList.get(chessClassList.size() - 1);
		redCircle.setCenterX(chessClass1.getX() * 40 + 50);
		redCircle.setCenterY(chessClass1.getY() * 40 + 50);
		redCircle.setFill(Color.RED);
		pane.getChildren().add(redCircle);
	}

	private void startTimelines() {
		gameTimeline.play();
		stepTimeline.play();
	}

	private void s2Alert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message);
		alert.initOwner(this);
		alert.setTitle("Reply");
		alert.setHeaderText("Sorry");
		ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(confirmButton);
		alert.show();
	}

	/**
	 * Handle new game message
	 *
	 * @param message
	 */
	private void newGameMessage(Message message) {
		NewGameMessage newGameMessage = (NewGameMessage) message;
		if (newGameMessage.getState() == NewGameMessage.REQUEST) {
			if (this.isAccept) {
				newGameMessage.setState(NewGameMessage.NO);
				newGameMessage.setAccount(Information.account);
				NetMessage.sendMessage(newGameMessage, sinfoServiceDao.queryIPByAccount(newGameMessage.getAccount()).getAddress());
				return;
			}
			this.newGame(newGameMessage);
		} else if (newGameMessage.getState() == NewGameMessage.OK) {
			Information.oppoIP = sinfoServiceDao.queryIPByAccount(newGameMessage.getAccount()).getAddress();
			this.startNew(newGameMessage.getAccount());
		} else if (newGameMessage.getState() == NewGameMessage.NO) {
			waitText.setText("");
			s3Alert(newGameMessage.getAccount() + ", I don't want to play with you again!");
		}
	}

	/**
	 * Get opponent's record message
	 *
	 * @param message
	 */
	private void rivalRecordMessage(Message message) {
		RivalRecordMessage recordMessage = (RivalRecordMessage) message;
		if (recordMessage.getRivalRecord() == RivalRecordMessage.REQUEST) {
			recordMessage.setRivalRecord(RivalRecordMessage.RESPONSE);
			User user = userServiceDao.queryUserByAccount(Information.account);
			recordMessage.setUser(user);
			NetMessage.sendMessage(recordMessage, Information.temporaryOppoIP);
		} else if (recordMessage.getRivalRecord() == RivalRecordMessage.RESPONSE) {
			OpponentRecord.recordMessage = recordMessage;
			OpponentRecord record = new OpponentRecord();
			record.show();
		}
	}

	private void s3Alert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message);
		alert.initOwner(this);
		alert.setTitle("Reply");
		alert.setHeaderText("Sorry");
		ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(confirmButton);
		alert.show();
	}

	/**
	 * Game over message
	 */
	/**
	 * Result message
	 * @param message
	 */
	private void resultMessage(Message message) {
		ResultMessage resultMessage = (ResultMessage) message;
		this.player1 = Information.account;
		this.player2 = resultMessage.getAccount();
		this.winner = this.player1;

		// Clear opponent's IP
		Information.oppoIP = null;

		Record record = new Record();
		// If isLose() is true, it indicates a message after surrender, otherwise it's a message after a normal five-in-a-row
		// Black wins or black draw
		if (resultMessage.getResult() == ResultMessage.BLACK_WIN ||
				resultMessage.getResult() == ResultMessage.BLACK_DRAW) {
			record.setBlack(resultMessage.isLose() ? Information.account : resultMessage.getAccount());
			record.setWhite(resultMessage.isLose() ? resultMessage.getAccount() : Information.account);
			if (resultMessage.isLose()) {
				this.winner = this.player1;
				//System.out.println("1!");
			} else {
				this.winner = this.player2;
				//System.out.println("3!");
			}

			// White wins or white draw
		} else {
			record.setWhite(resultMessage.isLose() ? Information.account : resultMessage.getAccount());
			record.setBlack(resultMessage.isLose() ? resultMessage.getAccount() : Information.account);
			this.winner = this.player2;
			if (resultMessage.isLose()) {
				this.winner = this.player1;
				//System.out.println("2!");
			} else {
				this.winner = this.player2;
				//System.out.println("4!");
			}
		}

		// Record the result
		record.setResult(resultMessage.getResult() == 4 ? 3 : resultMessage.getResult());
		// Record the end time of the game
		record.setChessTime(new Timestamp(System.currentTimeMillis()));

		int result = record.getResult();
		Connection conn = null;
		try {
			// Get connection
			conn = JdbcConnection.getConnection();
			// Set transaction to manual commit
			JdbcConnection.disableAutocommit(conn);
			// Save the game record to the database
			recordServiceDao.saveRecord(record);
			// Update the records of both players
			// Black wins
			if (result == ResultMessage.BLACK_WIN) {
				// Add one point, increment total games played and wins
				String blackAccount = resultMessage.isLose() ? Information.account : resultMessage.getAccount();
				userServiceDao.saveWinChessByAccount(blackAccount);

				// Deduct one point, increment total games played and losses
				String whiteAccount = resultMessage.isLose() ? resultMessage.getAccount() : Information.account;
				userServiceDao.saveLostChessByAccount(whiteAccount);

				// White wins
			} else if (result == ResultMessage.WHITE_WIN) {
				// Add one point, increment total games played and wins
				String whiteAccount = resultMessage.isLose() ? Information.account : resultMessage.getAccount();
				userServiceDao.saveWinChessByAccount(whiteAccount);
				// Deduct one point, increment total games played and losses
				String blackAccount = resultMessage.isLose() ? resultMessage.getAccount() : Information.account;

				userServiceDao.saveLostChessByAccount(blackAccount);
				// Draw
			} else {
				// Increment total games played and draws
				String whiteAccount = resultMessage.isLose() ? Information.account : resultMessage.getAccount();
				userServiceDao.saveDrawChessByAccount(whiteAccount);
				this.winner = "Deuce";

				String blackAccount = resultMessage.isLose() ? resultMessage.getAccount() : Information.account;
				userServiceDao.saveDrawChessByAccount(blackAccount);
			}
			// Commit the transaction
			JdbcConnection.commit(conn);
		} catch (Exception e) {
			// Rollback the transaction
			JdbcConnection.rollback(conn);
		} finally {
			if (conn != null) {
				// Close the connection
				JdbcConnection.close(conn);
			}
		}
	}

	/**
	 * Battle request message
	 * @param message
	 */
	private void gameRequestMeaasge(Message message) {
		GameRequestMeaasge gameRequestMeaasge = (GameRequestMeaasge) message;

		// If it is a request message
		if (gameRequestMeaasge.getRequestType() == GameRequestMeaasge.GAME_REQUEST) {
			if (this.isAccept) {
				// Our side is currently accepting a game request
				// Decline the request
				gameRequestMeaasge.setRequestType(GameRequestMeaasge.GAME_REFUSE);
				// Send message
				NetMessage.sendMessage(gameRequestMeaasge, sinfoServiceDao.queryIPByAccount(gameRequestMeaasge.getAccount()).getAddress());
				return;
			}
			// Game request accepted
			this.isAccept = true;
			// Update opponent's IP
			Information.oppoIP = sinfoServiceDao.queryIPByAccount(gameRequestMeaasge.getAccount()).getAddress();
			Alert alert = new Alert(AlertType.CONFIRMATION, gameRequestMeaasge.getAccount() + " is requesting a battle, do you agree?",
					new ButtonType("NO", ButtonData.NO),
					new ButtonType("YES", ButtonData.YES));
			alert.initOwner(this);
			alert.setTitle("Request");
			alert.setHeaderText("Message: ");
			Optional<ButtonType> button = alert.showAndWait();
			// If agreed
			if (button.get().getButtonData() == ButtonData.YES) {
				this.stopThread();
				// Inform the previous opponent (the one who just made a move) to give up (clear temporary opponent IP)
				if (Information.temporaryOppoIP != null) {
					// Send message
					NetMessage.sendMessage(new EscapeMessage(), Information.temporaryOppoIP);
				}
				// Update temporary opponent IP
				Information.temporaryOppoIP = Information.oppoIP;

				// Randomly select chess piece color
				this.selectColor();

				// Game initialization
				this.startNew(gameRequestMeaasge.getAccount());

				// Put own account in message
				gameRequestMeaasge.setAccount(Information.account);
				// Send message
				gameRequestMeaasge.setRequestType(GameRequestMeaasge.GAME_AGREE);
				NetMessage.sendMessage(gameRequestMeaasge, Information.oppoIP);

			} else {
				// Update opponent's IP
				Information.oppoIP = sinfoServiceDao.queryIPByAccount(gameRequestMeaasge.getAccount()).getAddress();
				// Not agreed, revert to not accepting game requests
				this.isAccept = false;
				// If declined
				gameRequestMeaasge.setRequestType(GameRequestMeaasge.GAME_REFUSE);

				// Send message
				NetMessage.sendMessage(gameRequestMeaasge, Information.oppoIP);
				// Remove opponent's IP
				Information.oppoIP = null;
			}

			// Game request agreed
		} else if (gameRequestMeaasge.getRequestType() == GameRequestMeaasge.GAME_AGREE) {
			this.stopThread();
			// Inform the previous opponent (the one who just made a move) to give up (clear temporary opponent IP)
			if (Information.temporaryOppoIP != null) {
				// Send message
				NetMessage.sendMessage(new EscapeMessage(), Information.temporaryOppoIP);
			}
			// Update temporary opponent IP
			Information.temporaryOppoIP = Information.oppoIP;

			// Initialize data
			this.startNew(gameRequestMeaasge.getAccount());
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText(gameRequestMeaasge.getAccount() + " agreed to the match, let's start the game!");
			alert.initOwner(this);
			alert.setTitle("Reply");
			alert.setHeaderText("Let's go! ");
			ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
			alert.getButtonTypes().setAll(confirmButton);
			alert.show();

			// Game request declined
		} else if (gameRequestMeaasge.getRequestType() == GameRequestMeaasge.GAME_REFUSE) {

			// Revert to requesting game state after rejection
			this.isSend = !isSend;
			// Clear opponent's IP
			Information.oppoIP = null;
			// Remove
			this.waitText.setText("");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("The opponent has rejected your request!");
			alert.initOwner(this);
			alert.setTitle("Result");
			alert.setHeaderText("Sorry");
			ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
			alert.getButtonTypes().setAll(confirmButton);
			alert.show();
		}
	}

	/**
	 * Escape message
	 *
	 * @param message
	 */
	private void escapeMessage(Message message) {
		// Opponent escaped, can accept new game requests
		this.isAccept = false;
		// Clear temporary opponent IP
		Information.temporaryOppoIP = null;
		// If the game is not over, handle opponent's escape
		if (!this.gameOver) {
			this.specialWin("The opponent has run away.");
		}
	}

	/**
	 * Select chess color message
	 *
	 * @param message
	 */
	private void selectChessColorMessage(Message message) {
		SelectChessColorMessage selectChessColor = (SelectChessColorMessage) message;

		// If the color is white
		if (selectChessColor.getColor() == SelectChessColorMessage.WHITE) {
			// We play with white pieces
			this.blackOrWhite = 0;
			this.color = Color.ALICEBLUE;
			this.isPlay = false;
		}
		// If the color is black
		else if (selectChessColor.getColor() == SelectChessColorMessage.BLACK) {
			// Start the game and step timers
			gameTimeline.play();
			stepTimeline.play();
			// We play with black pieces
			this.blackOrWhite = 1;
			this.color = Color.BLACK;
			this.isPlay = true;
		}
	}

	/**
	 * Handle network communication messages, implement synchronous change of chess pieces on the board
	 *
	 * @param message
	 */
	public void upDateUI(Message message) {
		if (message instanceof ChessMessage) {
			this.chessMessage(message);
		} else if (message instanceof ReplayMessage) {
			this.replayMessage(message);
		} else if (message instanceof NewGameMessage) {
			this.newGameMessage(message);
		} else if (message instanceof RivalRecordMessage) {
			this.rivalRecordMessage(message);
		} else if (message instanceof ResultMessage) {
			this.resultMessage(message);
		} else if (message instanceof GameRequestMeaasge) {
			this.gameRequestMeaasge(message);
		} else if (message instanceof EscapeMessage) {
			this.escapeMessage(message);
		} else if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			textArea.appendText(textMessage.getAccount() + "：" + textMessage.getTextMessage() + "\n");
		} else if (message instanceof SelectChessColorMessage) {
			this.selectChessColorMessage(message);
		} else if (message instanceof GiveUpMessage) {
			this.specialWin("The opponent has surrendered.");
		} else if (message instanceof CountdownMessage) {
			this.specialWin("The opponent has timed out");
		}
	}

	/**
	 * Special handling logic after victory
	 */
	private void specialWin(String winMethod) {
		// Pause game and step timers
		this.gameTimeline.pause();
		this.stepTimeline.pause();
		this.winner = Information.account;
		// Change to idle status
		sinfoServiceDao.updateStatusByAccount(Information.account, 1);
		// Clear the VS text and chess pieces of both sides on the board
		// Re-add refresh, previous page and next page buttons
		this.removeEndTextAndCircle();
		// Game over
		this.gameOver = true;
		// Opponent surrendered, can send new game requests
		this.isSend = false;
		// Opponent surrendered, can accept new game requests
		this.isAccept = false;
		this.winner = this.player1;

		// Clear opponent IP
		Information.oppoIP = null;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(winMethod + "，Congratulations on your victory! Score +1.");
		alert.initOwner(this);
		alert.setTitle("Result");
		alert.setHeaderText("Good job! ");
		ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(confirmButton);
		alert.show();
	}

	/**
	 * Special handling logic after defeat
	 */
	private void specialLose() {
		// Pause game and step timers
		this.gameTimeline.pause();
		this.stepTimeline.pause();
		// Change to idle status
		sinfoServiceDao.updateStatusByAccount(Information.account, 1);

		// Clear the VS text and chess pieces of both sides on the board
		// Re-add refresh, previous page and next page buttons
		this.removeEndTextAndCircle();
		// Game over
		this.gameOver = true;
		// Opponent surrendered, can send new game requests
		this.isSend = false;
		// Opponent surrendered, can accept new game requests
		this.isAccept = false;
		this.winner = this.player2;
		//this.winner = addressService.queryAccountByIP(Global.oppoIP);
		// Clear opponent IP
		Information.oppoIP = null;
		Alert loseAlert = new Alert(AlertType.INFORMATION);
		loseAlert.setContentText("Game lost! Score -1.");
		loseAlert.initOwner(this);
		loseAlert.setTitle("Result");
		loseAlert.setHeaderText("Oops!");
		ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
		loseAlert.getButtonTypes().setAll(confirmButton);
		loseAlert.show();
	}

	/**
	 * Clear the VS text and chess pieces of both sides on the board
	 * Re-add refresh, previous page and next page buttons
	 */
	private void removeEndTextAndCircle() {
		// Clear the VS text and chess pieces of both sides on the board
		pane.getChildren().removeAll(startTextList);

		pane.getChildren().remove(myAccountCircle);
		pane.getChildren().remove(opponentAccountCircle);
		pane.getChildren().remove(currentChess);

		// Display online player text on the board
		this.addPlay();

		// Online player previous page button
		this.prePage();

		// Online player next page button
		this.nextPageButton();
	}

	// Remove online player information on the board, and related title buttons
	private void removeUserInfo() {
		// Remove online players on the board
		if (!textList.isEmpty()) {
			pane.getChildren().removeAll(this.textList);
			textList.clear();
		}
		// Remove online player prompt text
		pane.getChildren().remove(matchText);

		// Remove refresh, previous page, next page buttons
		pane.getChildren().remove(prePage);
		pane.getChildren().remove(nextPageButton);
	}

	/**
	 * New game
	 *
	 * @param newGameMessage
	 */
	private void newGame(NewGameMessage newGameMessage) {
		// Game request accepted
		this.isAccept = true;
		Alert alert = createAlert1(AlertType.CONFIRMATION, "The opponent requests a rematch, do you agree?");
		Optional<ButtonType> button = alert.showAndWait();
		if (button.get().getButtonData() == ButtonData.YES) {
			// Get opponent's IP
			Information.oppoIP = sinfoServiceDao.queryIPByAccount(newGameMessage.getAccount()).getAddress();
			// Randomly select chess color
			this.selectColor();
			// Initialize new game
			this.startNew(newGameMessage.getAccount());
			// After confirming new game, send message to notify opponent to initialize
			sendNewGameMessage(newGameMessage, NewGameMessage.OK);
		} else {
			// After refusal, it becomes no game request accepted
			this.isAccept = false;
			// If disagree, send message to notify opponent
			sendNewGameMessage(newGameMessage, NewGameMessage.NO);
		}
	}

	/**
	 * Randomly select chess color
	 */
	public void selectColor() {
		// Random 0 or 1
		int randomNum = (int) (Math.random() * 2);
		// If 1, we play with black pieces, send message to notify opponent: you are white
		if (randomNum == 1) {
			// Start game and step timers
			gameTimeline.play();
			stepTimeline.play();
			this.blackOrWhite = 1;
			this.color = Color.BLACK;
			this.isPlay = true;
			sendSelectChessColorMessage(SelectChessColorMessage.WHITE);
			// If 0, we play with white pieces, send message to notify opponent: you are black
		} else {
			this.blackOrWhite = 0;
			this.color = Color.ALICEBLUE;
			this.isPlay = false;
			sendSelectChessColorMessage(SelectChessColorMessage.BLACK);
		}
	}

	private Alert createAlert1(AlertType type, String content) {
		Alert alert = new Alert(type, content,
				new ButtonType("NO", ButtonData.NO),
				new ButtonType("YES", ButtonData.YES));
		alert.initOwner(this);
		alert.setTitle("Request");
		alert.setHeaderText("Request: ");
		return alert;
	}

	private void sendNewGameMessage(NewGameMessage newGameMessage, int state) {
		newGameMessage.setState(state);
		newGameMessage.setAccount(Information.account);
		// Send message
		NetMessage.sendMessage(newGameMessage, Information.temporaryOppoIP);
	}

	private void sendSelectChessColorMessage(int color) {
		SelectChessColorMessage selectChessColor = new SelectChessColorMessage();
		selectChessColor.setColor(color);
		// Send message
		NetMessage.sendMessage(selectChessColor, Information.temporaryOppoIP);
	}


	/**
	 * Place a piece
	 *
	 * @param x
	 * @param y
	 */
	private void piece(int x, int y) {
		if (chessClassList.size() == row_num * row_num) {
			System.out.println("The board is full, the game is over.");
			// Draw
			// Send a message to the opponent to update the database information
			ResultMessage resultMessage = new ResultMessage();
			// Set the result property of the message class based on the color of the current user's piece
			resultMessage.setResult(Color.BLACK.equals(this.color) ?
					ResultMessage.BLACK_DRAW : ResultMessage.WHITE_DRAW);

			// Display prompt box
			chessFullReminder();
			return;
		}

		// Check if the piece is repeated
		if (arr[x][y]) {
			System.out.println("Invalid move, same coordinate already occupied!");
			return;
		}

		if (isForbiddenMove) {
			ChessClass ac = new ChessClass(x, y, isBlack ? Color.BLACK : Color.WHITE);
			if (ForbiddenMove.forbiddenMove(chessClassList, ac, arr, colors)) {
				System.out.println("Forbidden Move!");
				return;
			}
		}

		// Play the sound of placing a piece
		this.soundMoveLater();
		// The current thread sleeps for 20 milliseconds
		this.stopThread();

		// Pause the game timer
		// Pause and reset the step timer
		stepTimeline.pause();
		this.stepTimeText.setText("Move time limit 60");
		this.stepTimeNum = 60;

		// Remove the previous red mark
		if (!chessClassList.isEmpty()) {
			pane.getChildren().remove(redCircle);
		}

		// The color of the piece behind the current drop text
		currentChess.setFill(isBlack ? Color.ALICEBLUE : Color.BLACK);

		// After placing a piece, you have to wait for the opponent to place a piece before you can continue to place a piece
		isPlay = !isPlay;
		arr[x][y] = true;
		int tempX = x * spacing + 50;
		int tempY = y * spacing + 50;
		// Draw a piece
		Circle circle = new Circle();
		// The x-coordinate of the piece landing point
		circle.setCenterX(tempX);
		// The y-coordinate of the piece landing point
		circle.setCenterY(tempY);

		// Set the color of the piece
		circle.setFill(this.color);
		// Record the color of the piece in the array colors
		colors[x][y] = this.color;

		// Set the radius of the piece
		circle.setRadius(chess_size);

		// Add the piece to the board
		pane.getChildren().add(circle);

		// The x-coordinate of the landing point mark
		redCircle.setCenterX(tempX);
		// The y-coordinate of the landing point mark
		redCircle.setCenterY(tempY);
		// Set to red
		redCircle.setFill(Color.RED);
		// Add the mark to the board
		pane.getChildren().add(redCircle);

		// Save the information of the piece to the array
		ChessClass chessClass = new ChessClass(x, y, this.color);
		chessClassList.add(chessClass);
		// Change the color of the piece
		isBlack = !isBlack;
		// Send a message to the server
		NetMessage.sendMessage(new ChessMessage(x, y, this.blackOrWhite), Information.oppoIP);
		// If there are five consecutive pieces, the game is over
		if (isWin(chessClass)) {
			// Stop the game timer
			gameTimeline.pause();
			// Stop the step timer
			stepTimeline.pause();

			// If there are five consecutive pieces, send a message to the opponent, and the opponent will update the database
			ResultMessage resultMessage = new ResultMessage();
			// Set to not surrender
			resultMessage.setLose(false);
			resultMessage.setAccount(Information.account);
			resultMessage.setResult(Color.BLACK.equals(this.color) ?
					ResultMessage.BLACK_WIN : ResultMessage.WHITE_WIN);

			// Send the game result message
			NetMessage.sendMessage(resultMessage, Information.oppoIP);

			// Clear opponent's IP
			Information.oppoIP = null;

			// Clear the VS text and pieces of both sides on the board
			// Re-add refresh, previous page and next page buttons
			this.removeEndTextAndCircle();

			// Display the game over pop-up window
			this.gameOverReminder("Victory");
		}
	}

	// Prompt box when the board is full of pieces
	private void chessFullReminder() {
		gameOver = true;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(this);
		alert.setTitle("Game Result");
		alert.setHeaderText("Game Result: ");
		alert.setContentText("Tie!");
		ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(confirmButton);
		alert.show();
	}

	// Game over prompt box
	private void gameOverReminder(String result) {
		// Change to offline status
		sinfoServiceDao.updateStatusByAccount(Information.account, 1);
		// The game is over, you can re-initiate a game request to any opponent
		this.isSend = false;
		this.gameOver = true;
		// The game is over, can be applied for a game by the player
		this.isAccept = false;

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(this);
		alert.setTitle("Match result");
		alert.setHeaderText("Update: ");
		alert.setContentText("Victory".equals(result) ? "Victory, score +1" : "Defeat, score -1");
		ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(confirmButton);
		alert.show();
	}

	/**
	 * Whether there are five consecutive pieces
	 *
	 * @param chessClass
	 * @return
	 */

	private boolean isWin(ChessClass chessClass) {
		return GameRule.isWin(chessClassList, chessClass, arr, colors);
	}

	/**
	 * Add buttons
	 */
	private void addButton() {
		// New game button
		this.againButton();

		// Undo button
		this.withDrawButton();

		// Save game record button
		this.saveChessBookButton();

		// Open game record button
		this.openChessBookButton();

		// Exit button
		this.exitButton();

		this.humanVsAiButton();

		// View own information
		this.myRecordButton();

		// View opponent information
		this.rivalRecordButton();

		// Send message button
		this.messageButton();

		// Online players previous page button
		this.prePage();

		// Online players next page button
		this.nextPageButton();

		// Other dropdown button
		this.otherButton();
	}

	/**
	 * Dropdown button, contains some other buttons
	 */
	private void otherButton() {
		// Create dropdown button
		menuButton = new SplitMenuButton();
		menuButton.setText("Additional");
		menuButton.setPrefSize(100, 40);
		menuButton.setLayoutX(940);
		menuButton.setLayoutY(850);

		// Create slideshow button in dropdown
		MenuItem menuItemPlayImages = new MenuItem(null, playImagesButton());

		// Create surrender button in dropdown
		MenuItem menuItemgiveUp = new MenuItem(null, giveUpButton());

		// Create music slideshow button in dropdown
		MenuItem menuItemPlayMusics = new MenuItem(null, playMusicsButton());

		// Create pause/play background music button
		MenuItem menuMusic = new MenuItem(null, musicButton());

		// Create help AI button
		MenuItem menuRobot = new MenuItem(null, helpRobotButton());

		// Create forbidden move button
		MenuItem menuForbiddenMove = new MenuItem(null, forbiddenMoveButton());

		// Add buttons to dropdown
		menuButton.getItems().addAll(menuForbiddenMove, menuRobot, menuItemPlayImages, menuItemPlayMusics, menuMusic, menuItemgiveUp);
		pane.getChildren().add(menuButton);
	}

	/**
	 * Help AI button
	 */
	private Button helpRobotButton() {
		Button helpRobotButton = new Button("Hint");
		helpRobotButton.setPrefSize(80, 40);
		helpRobotButton.setOnAction(e -> {
			// Help button logic implementation
			this.helpRobot();
		});

		return helpRobotButton;
	}

	// Help button logic implementation
	private void helpRobot() {
		if (gameOver || !isPlay) {
			return;
		}
		if (hintNum == 0) {
			Alert alert1 = new Alert(AlertType.INFORMATION, "The number of your requests for assistance has been exhausted.");
			alert1.initOwner(this);
			alert1.setTitle("Warning");
			alert1.setHeaderText("Warning: ");
			alert1.show();
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION, "You have " + hintNum-- + " remaining assistance opportunities. Are you sure you want to request assistance?",
				new ButtonType("NO", ButtonData.NO),
				new ButtonType("YES", ButtonData.YES));
		alert.initOwner(this);
		alert.setTitle("Warning");
		alert.setHeaderText("Warning: ");
		Optional<ButtonType> button = alert.showAndWait();
		if (button.get().getButtonData() == ButtonData.YES) {
			if (chessClassList.isEmpty()) {
				this.piece(7, 7);
			} else {
				ChessClass chessClass = AIPlay.getChess(colors, arr, color, row_num);
				// Draw the robot's piece on the board
				this.piece(chessClass.getX(), chessClass.getY());
			}
		}
	}

	// Control the play and pause of background music
	private Button musicButton() {
		musicButton = new Button("Pause the music");
		musicButton.setPrefSize(120, 40);

		// Pause/play background music logic implementation
		musicButton.setOnAction(e -> {
			if (playMusic) {
				this.mediaPlayer.pause();
				playMusic = !playMusic;
				musicButton.setText("Play the music");
			} else {
				this.mediaPlayer.play();
				playMusic = !playMusic;
				musicButton.setText("Pause the music");
			}
		});
		return musicButton;
	}

	/**
	 * Forbidden move
	 */
	private Button forbiddenMoveButton() {
		Button forbiddenMoveButton = new Button("ForbiddenMove Off");
		forbiddenMoveButton.setPrefSize(160, 40);
		forbiddenMoveButton.setOnAction(e -> {

			if (!isForbiddenMove) {
				isForbiddenMove = !isForbiddenMove;
				forbiddenMoveButton.setText("ForbiddenMove On");
			} else {
				isForbiddenMove = !isForbiddenMove;
				forbiddenMoveButton.setText("ForbiddenMove Off");
			}
		});

		return forbiddenMoveButton;
	}

	/**
	 * Surrender button
	 */
	private Button giveUpButton() {
		Button giveUpButton = new Button("Surrender");
		giveUpButton.setPrefSize(80, 40);

		// Surrender button logic implementation
		giveUpButton.setOnAction(e -> {
			if (gameOver) {
				return;
			}

			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to surrender?",
					new ButtonType("NO", ButtonData.NO),
					new ButtonType("YES", ButtonData.YES));
			alert.initOwner(this);
			alert.setTitle("Warning");
			alert.setHeaderText("Warning: ");
			Optional<ButtonType> button = alert.showAndWait();
			if (button.get().getButtonData() == ButtonData.YES) {
				NetMessage.sendMessage(new GiveUpMessage(), Information.oppoIP);

				// Surrender, send a message to the opponent, the opponent will update the database
				ResultMessage resultMessage = new ResultMessage();
				resultMessage.setAccount(Information.account);
				resultMessage.setResult(Color.BLACK.equals(this.color) ?
						ResultMessage.WHITE_WIN : ResultMessage.BLACK_WIN);

				// Send the game result message
				NetMessage.sendMessage(resultMessage, Information.oppoIP);

				// Handling logic after surrender
				this.specialLose();
			}

		});

		return giveUpButton;
	}

	/**
	 * Image slideshow button
	 */
	private Button playImagesButton() {
		Button playImagesButton = new Button("Start Slide Show");
		playImagesButton.setPrefSize(120, 40);

		// Slideshow background image button logic implementation
		playImagesButton.setOnAction(e -> {
			if (playImages) {
				timeline1.stop();
				playImagesButton.setText("Start Slide Show");
			} else {
				timeline1.play();
				playImagesButton.setText("Pause Slide Show");
			}
			playImages = !playImages;
		});

		return playImagesButton;
	}

	private Button playMusicsButton() {
		Button playMusicsButton = new Button("Start BGM Show");
		playMusicsButton.setPrefSize(120, 40);

		// Slideshow background image button logic implementation
		playMusicsButton.setOnAction(e -> {
			if (playMusics) {
				timeline2.stop();
				playMusicsButton.setText("Start BGM Show");
			} else {
				timeline2.play();
				playMusicsButton.setText("Pause BGM Show");
			}
			playMusics = !playMusics;
		});

		return playMusicsButton;
	}

	/**
	 * Button for online players to go to the previous page
	 */
	private void prePage() {
		prePage = createButton("Previous Page", 910, 320, 100, 40);
		prePage.setOnAction(e -> {
			this.prevPage();
		});
	}

	/**
	 * Logic implementation for the previous page button
	 */
	private void prevPage() {
		if (index == 0) {
			s4Alert("This is the first page!");
			return;
		}
		// Display the second page of online players
		queryAllAccountShowSinfo(--index * user_num, user_num);
	}

	/**
	 * Button for online players to go to the next page
	 */
	private void nextPageButton() {
		nextPageButton = createButton("Next page", 1110, 320, 100, 40);
		nextPageButton.setOnAction(e -> {
			this.nextPage();
		});
	}

	/**
	 * Logic implementation for the next page button
	 */
	private void nextPage() {
		// Total number of current online players
		long totalPage = sinfoServiceDao.queryAllCount();
		// The maximum index that can be displayed by paging
		int nowIndex = (int) (totalPage - 1) / user_num;
		if (index == nowIndex) {
			s4Alert("This is the last page!");
			return;
		}
		// Display the second page of online players
		queryAllAccountShowSinfo(++index * user_num, user_num);
	}

	/**
	 * Button to send text messages to the opponent
	 */
	private void messageButton() {
		Button messageButton = createButton("Send", 1110, 800, 50, 25);
		messageButton.setOnAction(e -> {
			this.sendMessage();
		});
		// Button is associated with the Enter key
		messageButton.setDefaultButton(true);
	}

	/**
	 * Logic implementation for sending messages
	 */
	private void sendMessage() {
		String message = messageText.getText().trim();
		// Clear the input box after sending
		messageText.setText("");
		// Strings with a length greater than 0 and less than or equal to 30 can be sent successfully
		if (message.length() > 0 && message.length() <= 30) {
			textArea.appendText(Information.account + "：" + message + "\n");
			if (Information.temporaryOppoIP == null) {
				return;
			}
			TextMessage textMessage = new TextMessage();
			textMessage.setTextMessage(message);
			textMessage.setAccount(Information.account);

			// Send a message to the opponent
			NetMessage.sendMessage(textMessage, Information.temporaryOppoIP);
		} else if (message.length() <= 0) {
			s4Alert("Null message!");
		} else {
			s4Alert("The message length exceeds the limit.");
		}
	}

	/**
	 * Create a button with given parameters
	 */
	private Button createButton(String text, int x, int y, int width, int height) {
		Button button = new Button(text);
		button.setLayoutX(x);
		button.setLayoutY(y);
		button.setPrefSize(width, height);
		pane.getChildren().add(button);
		return button;
	}

	/**
	 * Show an alert with given message
	 */
	private void s4Alert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message);
		alert.initOwner(this);
		alert.setTitle("Warning");
		alert.setHeaderText("Warning: ");
		ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(confirmButton);
		alert.show();
	}

	/**
	 * New Game Button
	 */
	private void againButton() {
		Button button = createButton("New Game", 40, 850,80);
		pane.getChildren().add(button);
		button.setOnAction(e -> {
			if (gameOver) {
				if (Information.temporaryOppoIP == null) {
					Alert alert = new Alert(AlertType.INFORMATION, "No opponent available, unable to start a new game!");
					alert.initOwner(this);
					alert.setTitle("Warning");
					alert.setHeaderText("Warning: ");
					ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
					alert.getButtonTypes().setAll(confirmButton);
					alert.show();
					return;
				}
				this.waitText.setText("The opponent is thinking, please wait patiently...");
				NewGameMessage newGameMessage = new NewGameMessage();
				newGameMessage.setState(NewGameMessage.REQUEST);
				newGameMessage.setAccount(Information.account);

				NetMessage.sendMessage(newGameMessage, Information.temporaryOppoIP);
			} else {
				Alert alert = new Alert(AlertType.INFORMATION, "The game is not over, cannot restart.");
				alert.initOwner(this);
				alert.setTitle("Warning");
				alert.setHeaderText("Warning: ");
				ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
				alert.getButtonTypes().setAll(confirmButton);
				alert.show();
			}
		});
	}

	/**
	 * Initialize new game data
	 */
	private void startNew(String rivalAccount) {
		// Initialization logic goes here
		this.isBlack = true;
// Change game status
		this.sinfoServiceDao.updateStatusByAccount(Information.account, 2);
// Clear text after regret request, battle request, and new game request
		this.waitText.setText("");
// Remove player information on the online chessboard, and related title buttons
		this.removeUserInfo();

// Clear chess pieces
		this.pane.getChildren().removeIf(e -> e instanceof Circle);

// Add player account VS player account on the chessboard, followed by the player's black or white chess piece
// Add current move followed by a black or white chess piece
		this.addStartText(rivalAccount);

// Clear chess piece information
		this.chessClassList = new ArrayList<>();
// Enable regret
		this.replay = true;
		this.arr = new boolean[row_num][row_num];
		this.colors = new Color[row_num][row_num];

// Add five-point markers
		this.drawNinePoint();
		this.gameOver = false;
// Game started, cannot send battle requests anymore
		this.isSend = true;
// Reset game start time
		startTime = LocalDateTime.now();
		if (this.startTimeText == null) {
			this.startTimeText = new Text(170, 60, "Start time: " + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			this.pane.getChildren().add(this.startTimeText);
		} else {
			this.startTimeText.setText("Start time: " + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		}

		this.gameTimeText.setText("Elapsed time 00:00");
		this.gameTimeNum = 0;
// Reset move time
		this.stepTimeText.setText("Move time limit 60");
		this.stepTimeNum = 60;
// Reset number of times to ask for help from the chess spirit
		this.hintNum = 3;
// Call setupTimeUpdater here to start time updates
		this.player1 = Information.account;
		this.player2 = rivalAccount;
		this.winner = this.player1;
	}

	/**
	 * Add player accounts and current player to the board
	 *
	 * @param rivalAccount Opponent's account
	 */
	private void addStartText(String rivalAccount) {
		// My account
		Text MyAccountText = createText(885, 80, Information.account, Color.DARKBLUE, 25);

		// Chess piece after the account
		myAccountCircle.setFill(color);

		// VS
		Text vsText = createText(885, 150, "VS", Color.BLACK, 25);

		// Opponent's account
		Text rivalAccountText = createText(885, 220, rivalAccount, Color.RED, 25);

		// Chess piece after the opponent's account
		opponentAccountCircle.setFill(blackOrWhite == 1 ? Color.ALICEBLUE : Color.BLACK);

		// Current player
		Text nowChessText = createText(865, 290, "Current player", Color.RED, 25);

		// Chess piece of the current player
		currentChess.setFill(Color.BLACK);

		pane.getChildren().addAll(MyAccountText, vsText, rivalAccountText, nowChessText,
				myAccountCircle, opponentAccountCircle, currentChess);
		startTextList.add(MyAccountText);
		startTextList.add(vsText);
		startTextList.add(rivalAccountText);
		startTextList.add(nowChessText);
	}

	/**
	 * Human vs AI Button
	 */
	private void humanVsAiButton() {
		Button button = new Button("Human vs AI");
		button.setPrefSize(120, 40);
		button.setLayoutX(1060);
		button.setLayoutY(850);
		button.setOnAction(e -> {
			this.openHumanVsAiPage();
		});
		pane.getChildren().add(button);
	}

	// This method opens a new window for a human vs AI game
	private void openHumanVsAiPage() {
		// Open the chessboard interface
		ChessBoard_ai chessBoard_ai = new ChessBoard_ai();
		chessBoard_ai.show();
	}

	/**
	 * Exit Game Button
	 */
	private void exitButton() {
		Button button = createButton("Exit", 600, 850,50);
		button.setOnAction(e -> {
			this.exit();
		});
		pane.getChildren().add(button);
	}

	// Exit confirmation dialog
	private void exit() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to quit?",
				new ButtonType("NO", ButtonData.NO),
				new ButtonType("YES", ButtonData.YES));
		alert.initOwner(this);
		alert.setTitle("Warning");
		alert.setHeaderText("Warning: ");
		Optional<ButtonType> button = alert.showAndWait();
		if (button.get().getButtonData() == ButtonData.YES) {
			if (Information.temporaryOppoIP != null) {

				// Send escape message, if I escape, the opponent will win
				NetMessage.sendMessage(new EscapeMessage(), Information.temporaryOppoIP);

				if (!gameOver) {
					// If the game is not over and I escape, send a message to the opponent, the opponent will update the database
					ResultMessage resultMessage = new ResultMessage();
					resultMessage.setAccount(Information.account);
					resultMessage.setResult(Color.BLACK.equals(this.color) ?
							ResultMessage.WHITE_WIN : ResultMessage.BLACK_WIN);

					// Send the game result message
					NetMessage.sendMessage(resultMessage, Information.oppoIP);
				}
			}
			// Change to offline status
			sinfoServiceDao.updateStatusByAccount(Information.account, 0);
			// Exit game button
			System.exit(0);
		}
	}

	private Text createText(int layoutX, int layoutY, String text, Color color, int fontSize) {
		Text newText = new Text(layoutX, layoutY, text);
		newText.setFill(color);
		newText.setFont(Font.font("Times New Roman", FontPosture.REGULAR, fontSize));
		return newText;
	}

	/**
	 * Save Chess Book Button
	 */
	private void saveChessBookButton() {
		Button button = createButton("Save the game board", 260, 850,150);
		pane.getChildren().add(button);
		button.setOnAction(e -> {
			this.saveChessBook();
		});
	}

	/**
	 * Write the current chess book information into a file
	 */
	private void saveChessBook() {
		if (!gameOver) {
			s6Alert("The game is not over, cannot save the game record.");
			return;
		}
		if (chessClassList.isEmpty()) {
			s6Alert("The game board is empty, cannot save the game record.");
			return;
		}

		FileChooser fChooser = new FileChooser();
		fChooser.setTitle("Save the game board");
		fChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv", "*.csv"));

		File file = fChooser.showSaveDialog(this);
		if (file == null) {
			return;
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write("Start time: " + startTime);
			writer.newLine();
			LocalDateTime endTime = LocalDateTime.now();
			writer.write("Elapsed time: " + gameTimeNum);
			writer.newLine();
			writer.write("Player 1: " + this.player1);
			writer.newLine();
			writer.write("Player 2: " + this.player2);
			writer.newLine();
			writer.write("Winner: " + this.winner);
			writer.newLine();
			for (ChessClass chessClass : chessClassList) {
				writer.write(chessClass.getX() + "," + chessClass.getY() + "," + chessClass.getColor());
				writer.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open Chess Book Button
	 */
	private void openChessBookButton() {
		Button button = createButton("Open the game board", 430, 850,150);
		button.setOnAction(e -> {
			this.openChessBook();
		});
		pane.getChildren().add(button);
	}

	// Helper methods
	private Button createButton(String text, int layoutX, int layoutY,int width) {
		Button button = new Button(text);
		button.setPrefSize(width, 40);
		button.setLayoutX(layoutX);
		button.setLayoutY(layoutY);
		return button;
	}

	private void s6Alert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message);
		alert.initOwner(this);
		alert.setTitle("Warning");
		alert.setHeaderText("Warning: ");
		ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(confirmButton);
		alert.show();
	}

	/**
	 * Restore the game from a file
	 */
	private void openChessBook() {
		if (!gameOver) {
			Alert alert = new Alert(AlertType.INFORMATION, "The game is not over, cannot open the game record.");
			alert.initOwner(this);
			alert.setTitle("Warning");
			alert.setHeaderText("Warning: ");
			ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
			alert.getButtonTypes().setAll(confirmButton);
			alert.show();
			return;
		}
		// Change game status to in-game
		sinfoServiceDao.updateStatusByAccount(Information.account, 2);
		FileChooser fileChooser = new FileChooser();
		// Set the title of the save file dialog
		fileChooser.setTitle("Open the game board");
		// Set the default directory, can choose the directory after opening
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		// Set file suffix
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv", "*.csv"));
		// Open file save dialog
		File file = fileChooser.showSaveDialog(this);

		// Click cancel, cancel selection
		if (file == null) {
			// Change game status to idle
			sinfoServiceDao.updateStatusByAccount(Information.account, 1);
			return;
		}

		// Collection to store game record information
		List<ChessClass> chessClassList = new ArrayList<>();
		// Remove dropdown button
		pane.getChildren().remove(menuButton);
		// Clear pieces to ensure the correctness of the game record
		pane.getChildren().removeIf(e -> e instanceof Circle);
		// Clear all buttons
		pane.getChildren().removeIf(e -> e instanceof Button);
		// Clear all texts
		pane.getChildren().removeIf(e -> e instanceof Text);
		// Clear all single-line text boxes
		pane.getChildren().removeIf(e -> e instanceof TextField);
		// Clear multi-line text box
		pane.getChildren().removeIf(e -> e instanceof TextArea);

		// Add exit button
		this.exitButton();
		// Add five-point mark
		this.drawNinePoint();
		// Record the number of pieces in the game record
		int[] count = new int[1];
		// Set to not place pieces
		gameOver = true;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String startTimeString = reader.readLine();
			Text startTimeText = new Text(850, 30, startTimeString);
			startTimeText.setFill(Color.DARKBLUE);
			startTimeText.setFont(Font.font("Times New Roman",
					FontPosture.REGULAR, 25));
			pane.getChildren().add(startTimeText);

			// Read elapsed time
			String elapsedTimeString = reader.readLine();
			Text elapsedTimeText = new Text(850, 60, elapsedTimeString);
			elapsedTimeText.setFill(Color.DARKBLUE);
			elapsedTimeText.setFont(Font.font("Times New Roman",
					FontPosture.REGULAR, 25));
			pane.getChildren().add(elapsedTimeText);


			// Read player name
			String player1String = reader.readLine();
			Text player1Text = new Text(850, 90, player1String);
			player1Text.setFill(Color.DARKBLUE);
			player1Text.setFont(Font.font("Times New Roman",
					FontPosture.REGULAR, 25));
			pane.getChildren().add(player1Text);

			String player2String = reader.readLine();
			Text player2Text = new Text(850, 120, player2String);
			player2Text.setFill(Color.DARKBLUE);
			player2Text.setFont(Font.font("Times New Roman",
					FontPosture.REGULAR, 25));
			pane.getChildren().add(player2Text);

			// Read winner
			String winnerString = reader.readLine();
			Text winnerText = new Text(850, 150, winnerString);
			winnerText.setFill(Color.DARKBLUE);
			winnerText.setFont(Font.font("Times New Roman",
					FontPosture.REGULAR, 25));
			pane.getChildren().add(winnerText);

			String data;
			while ((data = reader.readLine()) != null) {
				// Store each line of data from the file into an array separated by commas, i.e., x y color
				String[] chessData = data.split(",");
				ChessClass chessClass = new ChessClass(Integer.parseInt(chessData[0]),
						Integer.parseInt(chessData[1]), Color.web(chessData[2]));
				chessClassList.add(chessClass);

			}
			// Next step button
			nextStepButton(count, chessClassList);

			// Previous step button
			previousStepButton(count, chessClassList);

			// End game record button
			exitChessBook();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Game record next step button
	 *
	 * @param count
	 * @param chessClassList
	 */
	private void nextStepButton(int[] count, List<ChessClass> chessClassList) {

		Button button = new Button("Next Step");
		button.setPrefSize(80, 40);
		button.setLayoutX(935);
		button.setLayoutY(210);
		pane.getChildren().add(button);
		// Next step button logic
		button.setOnAction(e -> {
			this.nextStep(count, chessClassList);
		});
	}

	/**
	 * Game record next step logic
	 *
	 * @param count
	 * @param chessClassList
	 */
	private void nextStep(int[] count, List<ChessClass> chessClassList) {
		if (count[0] == chessClassList.size()) {
			System.out.println("This is the last step!");
			return;
		}
		Circle circle = new Circle();
		ChessClass chessClass = chessClassList.get(count[0]);
		circle.setCenterX(chessClass.getX() * spacing + 50);
		circle.setCenterY(chessClass.getY() * spacing + 50);
		circle.setRadius(chess_size);
		circle.setFill(chessClass.getColor());
		pane.getChildren().add(circle);
		count[0]++;
	}

	/**
	 * Game record previous step button
	 *
	 * @param count
	 * @param chessClassList
	 */
	private void previousStepButton(int[] count, List<ChessClass> chessClassList) {
		Button button = new Button("Last Step");
		button.setPrefSize(80, 40);
		button.setLayoutX(935);
		button.setLayoutY(310);
		pane.getChildren().add(button);
		// Previous step button logic
		button.setOnAction(e -> {
			this.previousStep(count, chessClassList);
		});
	}

	/**
	 * Game record previous step logic
	 *
	 * @param count
	 * @param chessClassList
	 */
	private void previousStep(int[] count, List<ChessClass> chessClassList) {
		if (count[0] == 0) {
			System.out.println("This is the first step!");
			return;
		}
		// Remove the last step piece
		pane.getChildren().remove(pane.getChildren().size() - 1);
		count[0]--;
	}

	/**
	 * End game record button + logic
	 */
	private void exitChessBook() {
		Button button = new Button("Main Page");
		button.setPrefSize(80, 40);
		button.setLayoutX(935);
		button.setLayoutY(410);
		pane.getChildren().add(button);
		// Exit game record button logic
		button.setOnAction(e -> {
			// Change game status to idle
			sinfoServiceDao.updateStatusByAccount(Information.account, 1);
			// Remove all pieces
			pane.getChildren().removeIf(c -> c instanceof Circle);
			// Remove exit button
			pane.getChildren().removeIf(b -> b instanceof Button);
			// Remove text
			pane.getChildren().removeIf(t -> t instanceof Text);
			// Draw lines on the board, add five-point mark on the board, and display text
			this.addText();
			// Add text box
			this.addTextBox();
			// Add buttons
			this.addButton();
			// Empty piece information
			chessClassList = new ArrayList<ChessClass>();
			arr = new boolean[row_num][row_num];
			colors = new Color[row_num][row_num];
			// Reset black first
			isBlack = true;
			// Default piece color reset to black
			color = Color.BLACK;
			// Cannot place pieces
			isPlay = false;
			gameOver = true;

			// Can undo
			replay = true;
			// The initial color of the piece is black
			blackOrWhite = 1;
			return;
		});
	}

	/**
	 * Undo move button
	 */
	private void withDrawButton() {
		Button button = new Button("Undo move");
		button.setPrefSize(100, 40);
		button.setLayoutX(140);
		button.setLayoutY(850);

		// Add button to the board
		pane.getChildren().add(button);
		// Undo move logic
		button.setOnAction(e -> {
			this.withDraw();

		});
	}

	/**
	 * Undo move logic check
	 */
	private void withDraw() {
		// Game has already ended
		if (gameOver) {
			Alert alert = new Alert(AlertType.INFORMATION, "The game is already over, cannot undo the move!");
			alert.initOwner(this);
			alert.setTitle("Warning");
			alert.setHeaderText("Warning: ");
			ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
			alert.getButtonTypes().setAll(confirmButton);
			alert.show();
			return;
		}
		// Board is empty
		if (chessClassList.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION, "The game board is empty, cannot undo the move!");
			alert.initOwner(this);
			alert.setTitle("Warning");
			alert.setHeaderText("Warning: ");
			ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
			alert.getButtonTypes().setAll(confirmButton);
			alert.show();
			return;
		}
		// User has already undone the move
		if (!replay) {
			Alert alert = new Alert(AlertType.INFORMATION, "The move has already been undone, cannot undo the move again!");
			alert.initOwner(this);
			alert.setTitle("Warning");
			alert.setHeaderText("Warning: ");
			ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
			alert.getButtonTypes().setAll(confirmButton);
			alert.show();
			return;
		}
		// It's your turn to move
		if (isPlay) {
			Alert alert = new Alert(AlertType.INFORMATION, "You made the move yourself, cannot undo the move!");
			alert.initOwner(this);
			alert.setTitle("Warning");
			alert.setHeaderText("Warning: ");
			ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
			alert.getButtonTypes().setAll(confirmButton);
			alert.show();
			return;
		}

		this.waitText.setText("The request to undo the move has been sent to the opponent, please wait patiently...");
		// Send undo move request
		ReplayMessage replayMessage = new ReplayMessage();
		replayMessage.setFlag(ReplayMessage.REPLAY_REQUEST);
		NetMessage.sendMessage(replayMessage, Information.oppoIP);

	}

	/**
	 * View own record
	 */
	private void myRecordButton() {
		Button myRecordButton = new Button("My record");
		myRecordButton.setPrefSize(80, 40);
		myRecordButton.setLayoutX(670);
		myRecordButton.setLayoutY(850);
		pane.getChildren().add(myRecordButton);

		myRecordButton.setOnAction(e -> {
			// Display my record page
			MyRecord myRecord = new MyRecord();
			myRecord.show();
		});
	}

	/**
	 * View opponent's record
	 */
	private void rivalRecordButton() {
		Button rivalRecordButton = new Button("Opponent's record");
		rivalRecordButton.setPrefSize(150, 40);
		rivalRecordButton.setLayoutX(770);
		rivalRecordButton.setLayoutY(850);
		pane.getChildren().add(rivalRecordButton);

		rivalRecordButton.setOnAction(e -> {
			if (Information.temporaryOppoIP == null) {
				Alert alert = new Alert(AlertType.INFORMATION, "No connection to the opponent, unable to view.");
				alert.initOwner(this);
				alert.setTitle("Warning");
				alert.setHeaderText("Warning: ");
				ButtonType confirmButton = new ButtonType("OK", ButtonData.OK_DONE);
				alert.getButtonTypes().setAll(confirmButton);
				alert.show();
				return;
			}
			// Send view record message to opponent
			RivalRecordMessage recordMessage = new RivalRecordMessage();
			recordMessage.setRivalRecord(RivalRecordMessage.REQUEST);

			NetMessage.sendMessage(recordMessage, Information.temporaryOppoIP);
		});
	}

	/**
	 * Move sound
	 */
	private void soundMoveLater() {
		Media media = new Media(getClass().getResource("/music/Sound Effect.mp3").toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

	/**
	 * Current thread sleeps for 20 milliseconds, used to fix the unknown bug that occasionally there is no move sound after the piece is placed
	 */
	private void stopThread() {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}










