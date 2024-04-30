package GroupF2.gobang.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represents a record of a chess game, capturing details such as the players' identifiers,
 * the time of the game, and the game's result. This class implements Serializable to allow
 * its instances to be serialized for storage or transmission.
 *
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class Record implements Serializable {
	private static final long serialVersionUID = 1L; // Ensures version compatibility during serialization.

	private int id;              // Unique identifier for the game record.
	private String black;        // Identifier for the player controlling the black pieces.
	private String white;        // Identifier for the player controlling the white pieces.
	private Timestamp chessTime; // Timestamp of when the game occurred.
	private int result;          // Result of the game, possibly encoded as integers (e.g., 1 for black win, 0 for draw, -1 for white win).

	/**
	 * Gets the identifier of the player with the black pieces.
	 * @return the black player's identifier as a String.
	 */
	public String getBlack() {
		return black;
	}

	/**
	 * Sets the identifier of the player with the black pieces.
	 * @param black the new identifier for the black player as a String.
	 */
	public void setBlack(String black) {
		this.black = black;
	}

	/**
	 * Gets the identifier of the player with the white pieces.
	 * @return the white player's identifier as a String.
	 */
	public String getWhite() {
		return white;
	}

	/**
	 * Sets the identifier of the player with the white pieces.
	 * @param white the new identifier for the white player as a String.
	 */
	public void setWhite(String white) {
		this.white = white;
	}

	/**
	 * Gets the timestamp of when the chess game was played.
	 * @return the timestamp of the game.
	 */
	public Timestamp getChessTime() {
		return chessTime;
	}

	/**
	 * Sets the timestamp of when the chess game was played.
	 * @param chessTime the new timestamp for when the game occurred.
	 */
	public void setChessTime(Timestamp chessTime) {
		this.chessTime = chessTime;
	}

	/**
	 * Gets the result of the chess game.
	 * @return the result of the game as an integer.
	 */
	public int getResult() {
		return result;
	}

	/**
	 * Sets the result of the chess game.
	 * @param result the new result of the game, encoded as an integer.
	 */
	public void setResult(int result) {
		this.result = result;
	}

	/**
	 * Provides a string representation of this Record instance, useful for logging and debugging.
	 * @return a string representation of the Record object, detailing all fields.
	 */
	@Override
	public String toString() {
		return "Record [id=" + id + ", black=" + black + ", white=" + white + ", chessTime=" + chessTime + ", result=" + result + "]";
	}
}
