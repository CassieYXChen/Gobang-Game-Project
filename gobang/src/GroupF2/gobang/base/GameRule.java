package GroupF2.gobang.base;

import GroupF2.gobang.chessboard.ChessClass;
import javafx.scene.paint.Color;

import java.util.List;

public class GameRule {

	/**
	 * Determines five-in-a-row.
	 * chessList ：Collection of all chess pieces on the chessboard.
	 * chess： The current move.
	 * arr： Used to check if there is a chess piece on the chessboard.
	 * colors： Colors of all chess pieces.
	 * return： True if there is a five-in-a-row, false otherwise.
	 */
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

}