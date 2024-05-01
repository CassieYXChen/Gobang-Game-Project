package GroupF2.gobang.aiplayer;

import GroupF2.gobang.chessboard.ChessClass;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * It is used to obtain the best coordinate of the particle judged by the robot
 * @author CUHK CSCI3100 2023-24 Group F2
 *
 */
public class AIPlay {
	// Obtain the best coordinates of the robot

	/**
	 * @param arr    Used to determine whether there are chess pieces on the board at the specified coordinate
	 * @param color  The color of the robot drop
	 * @param colors The color of all the pieces
	 * @param num   The number of horizontal and vertical lines on a chessboard
	 * @Description
	 * @author CUHK CSCI3100 2023-24 Group F2
	 */
	public static ChessClass getChess(Color[][] colors, boolean[][] arr, Color color, int num) {
		return explore(colors, arr, color, num);
	}

	/**
	 * Gets the set of slots to score
	 * For each non-empty position, the Spaces around its rice font are added to the collection
	 * Take care to remove duplicates
	 *
	 * @param arr  Used to determine whether there are chess pieces on the board at the specified coordinate
	 * @param num The number of horizontal and vertical lines on a chessboard
	 * @return
	 * @Description
	 * @author CUHK CSCI3100 2023-24 Group F2
	 */
	private static List<ChessClass> getMoveSet(boolean[][] arr, int num) {
		List<ChessClass> MoveSet = new ArrayList<>();

		// Search the board for viable points, there is duplication,
		// Use addToList(List<RobotChess> allMayRobotChess, int x, int y) to remove the weight
		// The principle is to go through all the pieces on the board, and the empty space in the rice shape around it (except for the remaining eight in the middle) is the feasible point

		// Define eight possible directions: up, down, left, right, and four diagonal directions
		int[][] directions = {
				{-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Up, down, left, right
				{-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Four diagonal directions
		};

		// Traversing the board
		for (int i = 0; i < num; i++) {
			for (int j = 0; j < num; j++) {
				if (arr[i][j]) { // If there are pieces in the current position
					// Check all directions
					for (int[] direction : directions) {
						int newX = i + direction[0];
						int newY = j + direction[1];
						// Check that the new position is on the board and there are no pieces
						if (newX >= 0 && newX < num && newY >= 0 && newY < num && !arr[newX][newY]) {
							addToList(MoveSet, newX, newY);
						}
					}
				}
			}
		}
		return MoveSet;
	}

	/**
	 * For each non-empty position, find the position points around them (nine grids).
	 * This method is used to filter out duplicate points and add non-duplicate points to the collection
	 */
	private static void addToList(List<ChessClass> MoveSet, int x, int y) {
		// Use a stream to find out if a Chess object with the same x and y already exists
		boolean exists = MoveSet.stream()
				.anyMatch(chess -> chess.getX() == x && chess.getY() == y);

		// If it does not exist, a new Chess object is added to the list
		if (!exists) {
			MoveSet.add(new ChessClass(x, y));
		}
	}

	/**
	 * The position score is calculated according to the move type
	 *
	 * @param count       Link number
	 * @param leftStatus  Left side blocking case 1: empty space, 2: opponent or wall
	 * @param rightStatus Right side blocking situation 1: vacancy, 2: opponent or wall
	 * @return score
	 * @author CUHK CSCI3100 2023-24 Group F2
	 */
	private static int getScoreBySituation(int count, int leftStatus, int rightStatus) {
		int score = 0;

		// Five
		if (count >= 5)
			score += 200000;// Win

			// Four
		else if (count == 4) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 50000;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 3000;
			if (leftStatus == 2 && rightStatus == 2)
				score += 1000;
		}

		//Three
		else if (count == 3) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 3000;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 1000;
			if (leftStatus == 2 && rightStatus == 2)
				score += 500;
		}

		//Two
		else if (count == 2) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 500;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 200;
			if (leftStatus == 2 && rightStatus == 2)
				score += 100;
		}

		//One
		else if (count == 1) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 100;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 50;
			if (leftStatus == 2 && rightStatus == 2)
				score += 30;
		}

		return score;
	}


	private static int getDirectionalScore(int x, int y, Color color, Color[][] colors, int num, int dx, int dy) {
		Color myself = color;
		Color other = color.equals(Color.BLACK) ? Color.ALICEBLUE : Color.BLACK;
		colors[x][y] = myself;  // Simulate placing a stone

		int forwardStatus = 0, backwardStatus = 0;
		int count = 0;

		// Move in the forward direction
		int newX = x + dx, newY = y + dy;
		while (newX >= 0 && newX < num && newY >= 0 && newY < num) {
			if (myself.equals(colors[newX][newY])) {
				count++;
			} else {
				if (colors[newX][newY] == null)
					forwardStatus = 1; // Forward side is open
				else if (other.equals(colors[newX][newY]))
					forwardStatus = 2; // Forward side is blocked by opponent
				break;
			}
			newX += dx;
			newY += dy;
		}

		// Move in the backward direction
		newX = x - dx;
		newY = y - dy;
		while (newX >= 0 && newX < num && newY >= 0 && newY < num) {
			if (myself.equals(colors[newX][newY])) {
				count++;
			} else {
				if (colors[newX][newY] == null)
					backwardStatus = 1; // Backward side is open
				else if (other.equals(colors[newX][newY]))
					backwardStatus = 2; // Backward side is blocked by opponent
				break;
			}
			newX -= dx;
			newY -= dy;
		}

		colors[x][y] = null;  // Restore the board state
		return getScoreBySituation(count, forwardStatus, backwardStatus);
	}

	/**
	 * Returns vertical score at the specified board position.
	 */
	public static int getVerticalScore(int x, int y, Color color, Color[][] colors, int num) {
		return getDirectionalScore(x, y, color, colors, num, 0, 1);
	}

	/**
	 * Returns horizontal score at the specified board position.
	 */
	public static int getLevelScore(int x, int y, Color color, Color[][] colors, int num) {
		return getDirectionalScore(x, y, color, colors, num, 1, 0);
	}

	/**
	 * Returns diagonal score from top-left to bottom-right.
	 */
	public static int getSkewScore1(int x, int y, Color color, Color[][] colors, int num) {
		return getDirectionalScore(x, y, color, colors, num, 1, 1);
	}

	/**
	 * Returns diagonal score from top-right to bottom-left.
	 */
	public static int getSkewScore2(int x, int y, Color color, Color[][] colors, int num) {
		return getDirectionalScore(x, y, color, colors, num, 1, -1);
	}
	/**
	 * Is the space score with coordinates (x,y)
	 * @author CUHK CSCI3100 2023-24 Group F2
	 */
	private static int getScore(int x, int y, Color color, Color[][] colors, int num) {
		// Define the opponent's color
		Color otherColor = color.equals(Color.BLACK) ? Color.ALICEBLUE : Color.BLACK;

		// Use an array to manage the different score types
		int[] scores = new int[4];

		// Calculate scores in different directions
		scores[0] = getVerticalScore(x, y, color, colors, num) + getVerticalScore(x, y, otherColor, colors, num);
		scores[1] = getLevelScore(x, y, color, colors, num) + getLevelScore(x, y, otherColor, colors, num);
		scores[2] = getSkewScore1(x, y, color, colors, num) + getSkewScore1(x, y, otherColor, colors, num);
		scores[3] = getSkewScore2(x, y, color, colors, num) + getSkewScore2(x, y, otherColor, colors, num);

		// Sum all the scores to get the total score
		int totalScore = 0;
		for (int score : scores) {
			totalScore += score;
		}

		return totalScore;
	}


	/**
	 * Core AI method to analyze the board and find the optimal move.
	 * This algorithm finds positions beneficial for both players.
	 * @author CUHK CSCI3100 2023-24 Group F2
	 */
	private static ChessClass explore(Color[][] colors, boolean[][] available, Color color, int num) {
		// Retrieve all potential moves
		List<ChessClass> possibleMove = getMoveSet(available, num);

		int highestScore = Integer.MIN_VALUE;
		List<ChessClass> highestMove = new ArrayList<>();

		// Evaluate each potential move
		for (ChessClass move : possibleMove) {
			int score = getScore(move.getX(), move.getY(), color, colors, num);
			move.setScore(score);

			// Check if the current move has a higher score than previously found
			if (score > highestScore) {
				highestScore = score;
				highestMove.clear();  // Clear previous best moves as a new highest score is found
				highestMove.add(move);
			} else if (score == highestScore) {
				highestMove.add(move);  // Add to best moves if it matches the highest score
			}
		}

		// Randomly select one of the best moves
		if (!highestMove.isEmpty()) {
			int index = (int) (Math.random() * highestMove.size());
			ChessClass bestMove = highestMove.get(index);
			return new ChessClass(bestMove.getX(), bestMove.getY());
		}

		// Return null or a default position if no best move was found
		return null;  // Consider handling this scenario appropriately depending on your application's requirements
	}
}


