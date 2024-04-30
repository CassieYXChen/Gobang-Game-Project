package GroupF2.gobang.record;

import GroupF2.gobang.message.RivalRecordMessage;
import GroupF2.gobang.pojo.User;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Opponent's Record
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class OpponentRecord extends Stage {
	// Used to save the user information sent by the opponent, i.e., User class
	public static RivalRecordMessage recordMessage;

	public OpponentRecord() {
		// Set window properties
		this.setResizable(false);
		this.getIcons().add(new Image("images/logo.jpg"));
		this.setTitle("My Records");

		// Create and set the stage and scene
		Pane pane = new Pane();
		pane.setBackground(new Background(new BackgroundFill(Color.AZURE, null, null)));
		this.setScene(new Scene(pane, 400, 400));

		// Add text and button
		addText(pane);
		addButton(pane);
	}

	private void addText(Pane pane) {
		User user = recordMessage.getUser();
		String[] labels = {"Account: ", "Score: ", "Total count of games: ", "Win: ", "Defeat: ", "Tie: ", "Winning rate: "};
		Object[] values = {user.getAccount(), user.getScore(), user.getTotalNums(), user.getWinNums(), user.getLostNums(), user.getDrawNums()};
		double winRate = user.getWinNums() * 100.0 / (user.getTotalNums() == 0 ? 1 : user.getTotalNums());

		for (int i = 0; i < labels.length; i++) {
			Text text = new Text(80, 20 + i * 30, labels[i] + (i != labels.length - 1 ? values[i] : String.format("%.2f%%", winRate)));
			text.setFill(Color.BLACK);
			text.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 16));
			pane.getChildren().add(text);
		}
	}

	// Add button
	private void addButton(Pane pane) {
		// Add close button
		this.closeButton(pane);
	}

	// Close record page button
	private void closeButton(Pane pane) {
		Button closeButton = new Button("Close");
		closeButton.setPrefSize(50, 40);
		closeButton.setLayoutX(180);
		closeButton.setLayoutY(350);
		closeButton.setOnAction(e -> this.close());
		pane.getChildren().add(closeButton);
	}
}
