package GroupF2.gobang.chessboard;

import GroupF2.gobang.base.ForbiddenMove;
import GroupF2.gobang.aiplayer.AIPlay;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChessBoard_ai extends Stage {
    // Constants
    private static final int SIZE = 20;                 // Board size for the grid
    private static final int LINE_SPACING = 40;         // Spacing between lines on the board
    private static final int CHESS_RADIUS = 19;         // Radius of each chess piece

    // Game state variables
    private boolean isBlack = true;                     // Flag to check if the current player is black
    private boolean[][] arr = new boolean[SIZE][SIZE];  // Array to store the presence of chess pieces
    private Color[][] colors = new Color[SIZE][SIZE];   // Array to store the colors of chess pieces
    private List<ChessClass> chessClassList = new ArrayList<>();  // List to track all chess pieces on the board
    private boolean gameOver = false;                   // Flag to check if the game is over
    private boolean playImages;                         // Flag to control image playback in the game
    private int imageIndex;                             // Index to keep track of images for playback
    private boolean isForbiddenMove = false;            // Flag for checking illegal moves

    // UI components
    private Pane pane = new Pane();                     // Pane for holding UI components
    private Text startTimeText, ruleText, currentText;  // Text objects for displaying time, rules, and current status
    private Circle userChessColor, aiChessColor;        // Indicators for user and AI chess piece color
    private Button forbiddenMoveButton, newGameButton, playImagesButton; // Buttons for game options
    private Button randomButton, aiGameButton;          // Buttons for random moves and AI game mode

    // Timelines and images
    private Timeline timeline1;                         // Timeline for animations or scheduled tasks
    private List<ImageView> imageList1 = new ArrayList<>(); // List to store image views for animation

    // Game mode and settings
    private GameMode gameMode = GameMode.SELF;          // Current game mode, default is playing by oneself
    private LocalDateTime startTime;                    // Variable to track the start time of the game

    // Initialization of game mode enum
    private enum GameMode {                             // Enum to define different game modes
        SELF, RANDOM, AI_GAME                                // Possible modes: playing alone, random moves, and AI-driven
    }

    public ChessBoard_ai() {
        this.start();
    }

    private void start() {
        // Initialize the main pane.
        pane = new Pane();

        // Set up the scene with specified dimensions.
        Scene scene = new Scene(pane, 1400, 900);
        this.setScene(scene);

        // Make the window resizable.
        this.setResizable(true);

        // Set the title of the window.
        this.setTitle("Human vs AI");

        // Load and display an image.
        ImageView imageView = new ImageView("images/image7.jpg");
        pane.getChildren().add(imageView);

        // Initialize collections to manage images.
        imageList1 = new ArrayList<>();

        // Add background and other images.
        this.addImages();

        // Draw a nine-point grid on the scene.
        this.drawNinePoint();

        // Create a timeline for animations or other time-based events.
        this.createTimeline();

        // Handle mouse click events on the chessboard.
        this.mouseClickedChessboard();

        // Add auxiliary text to the scene for information or instructions.
        this.addAuxiliaryText();

        // Add control buttons to the scene.
        this.addControlButtons();

        // Draw lines or additional graphical elements.
        this.drawLine();
    }

    private void createTimeline() {
        // Dynamic carousel checkerboard background
        this.changeImagesTimeline();
    }

    private void drawLine() {
        // Drawing grid lines on the chessboard
        for (int i = 0; i < SIZE; i++) {
            // Calculate the offset for the current line
            int offset = 50 + LINE_SPACING * i;

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
        // Drawing nine points on the chessboard that are traditionally used in the game to provide reference points
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

    private void addAuxiliaryText() {
        // Record and display the start time
        displayStartTime();

        // Display game rules and instructions
        displayGameRules();
    }

    private void displayStartTime() {
        // Get the current time and format it for display
        startTime = LocalDateTime.now();
        String formattedTime = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        startTimeText = new Text(850, 100, "Start time: " + formattedTime);
        configureText(startTimeText, Color.DARKBLUE, "Times New Roman", FontPosture.REGULAR, 20);
    }

    private void displayGameRules() {
        // Define the rules text
        String rules = "1.Click \"New Game\" to begin a\nlocal two-player match by default.\n" +
                "2.Click \"Random\" for a single-player\nmatch against a random opponent.\n" +
                "3.Click \"AI Game\" for a single-player\nmatch against an AI opponent.\n" +
                "4.By default, your side plays as black\npieces.\n" +
                "5.After each match, click \"New Game\"\nto start a new game.";
        ruleText = new Text(850, 200, rules);
        configureText(ruleText, Color.DARKBLUE, "Times New Roman", FontPosture.REGULAR, 25);
    }

    private void configureText(Text textElement, Color color, String fontFamily, FontPosture posture, int size) {
        // Configure the common properties of text elements
        textElement.setFill(color);
        textElement.setFont(Font.font(fontFamily, posture, size));
        pane.getChildren().add(textElement); // Add text to the pane
    }

    private void addControlButtons() {
        // Add "New Game" button with its properties and event handler
        newGameButton = new Button("New Game");
        newGameButton.setPrefSize(140, 40);
        newGameButton.setLayoutX(850);
        newGameButton.setLayoutY(640);
        newGameButton.setOnAction(e -> restartGame());  // Action to restart the game

        // Add "Random" button to start a game in random mode
        randomButton = new Button("Random");
        randomButton.setPrefSize(140, 40);
        randomButton.setLayoutX(850);
        randomButton.setLayoutY(580); // Adjusted position to fit other buttons
        randomButton.setOnAction(e -> {
            gameMode = GameMode.RANDOM; // Set game mode to RANDOM
            //aiMove(); // Optionally call AI move method if needed
        });

        // Add "AI Game" button to start a game with AI
        aiGameButton = new Button("AI Game");
        aiGameButton.setPrefSize(140, 40);
        aiGameButton.setLayoutX(850);
        aiGameButton.setLayoutY(520); // Adjust position for alignment
        aiGameButton.setOnAction(e -> {
            gameMode = GameMode.AI_GAME; // Set game mode to AI_GAME
            aiGameMove(); // Call AI move method
        });

        // Add "Forbidden Move" button to toggle forbidden moves
        forbiddenMoveButton = new Button("Forbidden Move");
        forbiddenMoveButton.setPrefSize(140, 40);
        forbiddenMoveButton.setLayoutX(850);
        forbiddenMoveButton.setLayoutY(700);
        forbiddenMoveButton.setOnAction(e -> isForbiddenMove = true); // Enable forbidden moves

        // Add button to control image slideshow
        playImagesButton = new Button("Start Slide Show");
        playImagesButton.setPrefSize(140, 40);
        playImagesButton.setLayoutX(850);
        playImagesButton.setLayoutY(760);
        playImagesButton.setOnAction(e -> toggleSlideShow()); // Action to start or pause slideshow

        // Add all buttons to the pane
        pane.getChildren().addAll(playImagesButton, newGameButton, randomButton, aiGameButton, forbiddenMoveButton);

        // A function to perform additional actions (not shown here)
        exitChessBook();
    }

    private void toggleSlideShow() {
        // Toggle the state of the slide show between play and pause
        if (playImages) {
            timeline1.stop();
            playImagesButton.setText("Start Slide Show");
        } else {
            timeline1.play();
            playImagesButton.setText("Pause Slide Show");
        }
        playImages = !playImages;
    }
    private void changeImagesTimeline() {
        // Initialize the timeline for image slideshow with a 1-second interval
        timeline1 = new Timeline(new KeyFrame(Duration.millis(1000), e -> changeImages()));
        timeline1.setCycleCount(Timeline.INDEFINITE); // Set the timeline to repeat indefinitely

        // Start the timeline to begin the image slideshow and then immediately pause
        timeline1.play();  // Start the slideshow to ensure it is ready
        timeline1.pause(); // Pause the slideshow, to be controlled elsewhere
    }

    /**
     * Checkerboard background image carousel
     */
    private void changeImages() {
        if (imageIndex == imageList1.size()) {
            imageIndex = 0;
        }
        pane.getChildren().set(0, imageList1.get(imageIndex++));
    }

    /**
     * Put the background image into the collection and change the size to the same size as the main window
     */
    private void addImages() {
        // Use the for loop to traverse the image numbers from 1 to 8
        for (int i = 1; i <= 8; i++) {
            // Select the file extension according to the condition (.jpg for images 1, 3, 7,.png for others)
            String extension = (i == 1 || i == 3 || i == 7) ? ".jpg" : ".png";
            // Create an ImageView object and add it to the image list
            imageList1.add(new ImageView("images/image" + i + extension));
        }
    }

    private void exitChessBook() {
        // Create and configure the main page button
        Button button = createMainPageButton();

        // Add button to the pane
        pane.getChildren().add(button);

        // Set the action to close the application when the button is clicked
        button.setOnAction(event -> {
            // Remove all pieces and close the application
            closeApplication();
        });
    }

    /**
     * Creates and configures the main page button.
     * @return configured Button
     */
    private Button createMainPageButton() {
        Button button = new Button("Main Page");
        button.setPrefSize(140, 40);  // Set preferred size for the button
        button.setLayoutX(850);       // Set the X position for the button
        button.setLayoutY(820);       // Set the Y position for the button
        return button;
    }

    /**
     * Closes the application.
     */
    private void closeApplication() {
        this.close(); // Close the application window
    }

    private void mouseClickedChessboard() {
        pane.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();

            // Define the bounds of the chessboard, considering an offset of 10 pixels for margin
            final double offset = 40;
            final double maxBound = 820;

            // Check if the click is outside the bounds of the chessboard
            if (x < offset || x > maxBound || y < offset || y > maxBound) {
                return; // Do nothing if the click is out of bounds
            }

            // Calculate the indices of the intersection point on the chessboard grid
            int xIndex = (int) Math.round((x - 50) / 40);
            int yIndex = (int) Math.round((y - 50) / 40);

            // Create a Chess object at the calculated position with the current color
            ChessClass currentMove = new ChessClass(xIndex, yIndex, isBlack ? Color.BLACK : Color.WHITE);

            // Check if the move is valid and if the game is still ongoing
            if (!arr[xIndex][yIndex] && !gameOver) {
                if (isForbiddenMove) {
                    // Check for forbidden moves if enabled
                    if (!ForbiddenMove.forbiddenMove(chessClassList, currentMove, arr, colors)) {
                        placePieceAndCheckGameState(xIndex, yIndex);
                    }
                } else {
                    placePieceAndCheckGameState(xIndex, yIndex);
                }
            }
        });
    }

    /**
     * Places a chess piece on the board and checks the game state, also triggers AI movement based on game mode.
     */
    private void placePieceAndCheckGameState(int xIndex, int yIndex) {
        placePiece_checkGameState(xIndex, yIndex, isBlack ? Color.BLACK : Color.WHITE);
        triggerAIMove();
    }

    /**
     * Determines whether to trigger AI move based on the game mode.
     */
    private void triggerAIMove() {
        if (!gameOver) {
            switch (gameMode) {
                case RANDOM:
                    aiMove();
                    break;
                case AI_GAME:
                    aiGameMove();
                    break;
                case SELF:
                default:
                    // Do not make an AI move in SELF mode
                    break;
            }
        }
    }
    private void soundMoveLater() {
        Media media = new Media(getClass().getResource("/music/Sound Effect.mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    private void stopThread() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void placePiece_checkGameState(int x, int y, Color color) {
        // Place a chess piece on the board at the calculated position
        Circle circle = new Circle(50 + x * LINE_SPACING, 50 + y * LINE_SPACING, CHESS_RADIUS, color);
        pane.getChildren().add(circle);

        // Play the sound associated with placing a piece
        this.soundMoveLater();

        // Pause the current thread briefly to ensure smooth UI updates
        this.stopThread();

        // Create a new Chess object and add it to the list
        ChessClass chessClass = new ChessClass(x, y, color);
        chessClassList.add(chessClass);
        arr[x][y] = true;
        colors[x][y] = color;

        // Check if the current move wins the game or if the game is a draw
        if (isWin(chessClassList, chessClass, arr, colors)) {
            gameOver = true;
            showAlert("Game Over", (isBlack ? "Black" : "White") + " wins!");
        } else if (chessClassList.size() == SIZE * SIZE) {
            gameOver = true;
            showAlert("Game Over", "Tie!");
        }

        // Toggle the player turn
        isBlack = !isBlack;

        // Update UI elements or other components if necessary
        //updateChessColors();
    }
    public static boolean isWin(List<ChessClass> chessClassList, ChessClass chessClass, boolean[][] arr, Color[][] colors) {
        // If there are less than 9 pieces, five in a row is not possible
        if (chessClassList.size() < 9) {
            return false;
        }
        int[] dx = {-1, 0, -1, -1}; // direction of x
        int[] dy = {0, -1, -1, 1}; // direction of y

        for (int dir = 0; dir < 4; dir++) { // check four directions
            int count = 1; // current number of consecutive pieces
            for (int i = 1; i <= 4; i++) { // check four pieces in one direction
                int x = chessClass.getX() + dx[dir] * i;
                int y = chessClass.getY() + dy[dir] * i;
                if (x >= 0 && x <= 19 && y >= 0 && y <= 19 && arr[x][y] && chessClass.getColor().equals(colors[x][y])) {
                    count++;
                    //System.out.println(count);
                } else {
                    break;
                }
            }
            for (int i = 1; i <= 4; i++) { // check four pieces in the opposite direction
                int x = chessClass.getX() - dx[dir] * i;
                int y = chessClass.getY() - dy[dir] * i;
                if (x >= 0 && x <= 19 && y >= 0 && y <= 19 && arr[x][y] && chessClass.getColor().equals(colors[x][y])) {
                    count++;
                    //System.out.println(count);
                } else {
                    break;
                }
            }
            // check if there is five in a row
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }
    private void aiMove() {
        // Perform a simple AI move by randomly selecting an empty position on the chessboard
        Random random = new Random();
        int xIndex, yIndex;
        do {
            xIndex = random.nextInt(SIZE);
            yIndex = random.nextInt(SIZE);
        } while (arr[xIndex][yIndex]);  // Continue until an empty position is found

        // Place the piece and check game state
        placePiece_checkGameState(xIndex, yIndex, isBlack ? Color.BLACK : Color.WHITE);
    }

    private void restartGame() {
        // Remove all pieces from the pane (assuming pieces are represented by Circle nodes)
        pane.getChildren().removeIf(node -> node instanceof Circle);

        // Reset game data structures
        chessClassList.clear();
        arr = new boolean[SIZE][SIZE];
        colors = new Color[SIZE][SIZE];
        isBlack = true;
        gameOver = false;
        gameMode = GameMode.SELF;
        isForbiddenMove = false;

        // Redraw the initial setup or special points on the board
        drawNinePoint();
    }

    private void showAlert(String title, String content) {
        // Display an informational alert with a custom title and content
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);  // No header text
        alert.setContentText(content);
        ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(confirmButton);
        alert.showAndWait();
    }

    private void aiGameMove() {
        // Make a move using a more sophisticated AI logic, if the game is not over
        if (!gameOver) {
            ChessClass move = AIPlay.getChess(colors, arr, isBlack ? Color.BLACK : Color.WHITE, SIZE);
            if (move != null) {
                // If the AI finds a valid move, place the piece and check the game state
                placePiece_checkGameState(move.getX(), move.getY(), isBlack ? Color.BLACK : Color.WHITE);
            }
        }
    }
}

