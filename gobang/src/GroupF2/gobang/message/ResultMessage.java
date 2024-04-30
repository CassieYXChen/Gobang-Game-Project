package GroupF2.gobang.message;

/**
 * Class for game result messages
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class ResultMessage extends Message {
	// Indicates if it's a loss, default is true
	private boolean lose = true;
	// The account of the player
	private String account;
	// The result of the game, 1, 2, 3
	private int result;

	// Constants for game results
	public static final int BLACK_WIN = 1; // Black wins
	public static final int WHITE_WIN = 2; // White wins
	public static final int BLACK_DRAW = 3; // Black proposes a draw
	public static final int WHITE_DRAW = 4; // White proposes a draw

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public boolean isLose() {
		return lose;
	}

	public void setLose(boolean lose) {
		this.lose = lose;
	}
}

