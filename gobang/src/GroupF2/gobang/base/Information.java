package GroupF2.gobang.base;

import GroupF2.gobang.chessboard.ChessBoard;
import GroupF2.gobang.chessboard.ChessBoard_ai;

/**
 * Used to save user and game-related information
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class Information {
	// Own IP
	public static String myIP;
	// Own port number
	public static int myPort = 6667;
	// Opponent's IP
	public static String oppoIP;
	// Temporary opponent IP, used for new game
	public static String temporaryOppoIP;

	// Opponent's port number
	public static int oppoPort = 6666;
	// User account
	public static String account;
	// Chessboard information
	public static ChessBoard chessBoard;

	public static ChessBoard_ai chessBoard_ai;
}

