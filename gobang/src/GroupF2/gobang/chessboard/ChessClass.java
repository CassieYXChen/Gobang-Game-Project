package GroupF2.gobang.chessboard;

import javafx.scene.paint.Color;

/**
 * Represents a chess piece on the board. This class holds information about
 * the piece's color, its position on the board (x and y coordinates), and
 * a score that evaluates the effectiveness of a move when the piece is placed
 * by the "little chess fairy" (an automated or helper system).
 *
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class ChessClass {
	// X-coordinate of the chess piece on the board
	private int x;

	// Y-coordinate of the chess piece on the board
	private int y;

	// Color of the chess piece
	private Color color;

	// Score assigned to the chess piece by the evaluation algorithm when placed
	private int score;

	/**
	 * Returns the current score associated with the chess piece.
	 * This score typically represents the strategic value or potential impact of the piece's position.
	 *
	 * @return the score of the chess piece.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Sets the score for this chess piece.
	 * This method is typically used by an evaluation function to update the piece's strategic importance.
	 *
	 * @param score the new score to set.
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Returns the color of the chess piece.
	 *
	 * @return the color of the piece.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color of the chess piece.
	 * Color is used to distinguish between opposing players' pieces.
	 *
	 * @param color the new color to set for this piece.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Returns the x-coordinate of the chess piece on the board.
	 *
	 * @return the x-coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x-coordinate of the chess piece.
	 * This method updates the position of the piece horizontally on the board.
	 *
	 * @param x the new x-coordinate to set.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Returns the y-coordinate of the chess piece on the board.
	 *
	 * @return the y-coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y-coordinate of the chess piece.
	 * This method updates the position of the piece vertically on the board.
	 *
	 * @param y the new y-coordinate to set.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Constructs a Chess piece with specified x and y coordinates and color.
	 *
	 * @param x the x-coordinate of the chess piece.
	 * @param y the y-coordinate of the chess piece.
	 * @param color the color of the chess piece.
	 */
	public ChessClass(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	/**
	 * Constructs a Chess piece with specified x and y coordinates.
	 * The color needs to be set separately using the setColor method.
	 *
	 * @param x the x-coordinate of the chess piece.
	 * @param y the y-coordinate of the chess piece.
	 */
	public ChessClass(int x, int y) {
		super(); // Calls the constructor of the superclass (Object).
		this.x = x;
		this.y = y;
	}
}
