package GroupF2.gobang.message;

/**
 * Contains the x, y coordinates and color of a chess piece on the board
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class ChessMessage extends Message {
	private int x, y, blackOrWhite;

	public ChessMessage(int x, int y, int blackOrWhite) {
		this.x = x;
		this.y = y;
		this.blackOrWhite = blackOrWhite;
	}

	public int getBlackOrWhite() {
		return blackOrWhite;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}

