package GroupF2.gobang.record;

import GroupF2.gobang.serviceDao.UserServiceDaoImpl;
import GroupF2.gobang.base.Information;
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
 * My Record
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class MyRecord extends Stage{
	private UserServiceDaoImpl userServiceDao = new UserServiceDaoImpl();

	public MyRecord() {
		// Set window properties
		this.setResizable(false);
		this.getIcons().add(new Image("images/logo.jpg"));
		this.setTitle("My record");
		// Create and set the stage and scene
		Pane pane = new Pane();
		pane.setBackground(new Background(new BackgroundFill(Color.rgb(144, 202, 196),null,null)));
		Scene scene = new Scene(pane, 400, 400);
		this.setScene(scene);

		// Add text and button
		this.addText(pane);
		this.addButton(pane);
	}

	private void addText(Pane pane) {
		User user = userServiceDao.queryUserByAccount(Information.account);
		String[] labels = {"Account: ", "Score: ", "Total number of games: ", "Win: ", "Defeat: ", "Tie: ", "Winning rate: "};
		Object[] values = {user.getAccount(), user.getScore(), user.getTotalNums(), user.getWinNums(), user.getLostNums(), user.getDrawNums()};
		double winRate = user.getWinNums() * 100.0 / (user.getTotalNums() == 0 ? 1 : user.getTotalNums());

		for (int i = 0; i < labels.length; i++) {
			Text text = new Text(160, 20 + i * 30, labels[i] + (i != labels.length - 1 ? values[i] : String.format("%.2f%%", winRate)));
			text.setFill(Color.BLACK);
			text.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 15));
			pane.getChildren().add(text);
		}
	}
	private void addButton(Pane pane) {
		Button closeButton = new Button("Close");
		closeButton.setPrefSize(50, 40);
		closeButton.setLayoutX(180);
		closeButton.setLayoutY(350);
		closeButton.setOnAction(e -> this.close());
		pane.getChildren().add(closeButton);
	}
}
