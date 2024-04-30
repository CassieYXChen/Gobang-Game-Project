package GroupF2.gobang.serviceDao;

import GroupF2.gobang.pojo.User;

/**
 * Combined implementation class that handles both the service and data access
 * operations for User data.
 */
public class UserServiceDaoImpl extends BaseDao {

	/**
	 * Queries a user by their account identifier.
	 * @param account the account identifier
	 * @return the User object associated with the account, or null if not found
	 */
	public User queryUserByAccount(String account) {
		String sql = "SELECT id, account, `password`, regtime regTime, score, " +
				"totalnums totalNums, winnums winNums, lostnums lostNums, drawnums drawNums " +
				"FROM chess_user WHERE account = ?";
		return select(User.class, sql, account);
	}

	/**
	 * Queries a user by their account identifier and password for authentication purposes.
	 * @param account the user's account identifier
	 * @param password the user's password
	 * @return the User object if found and authenticated, null otherwise
	 */
	public User queryUserByAccountAndPassword(String account, String password) {
		String sql = "SELECT id, account, `password`, regtime regTime, " +
				"score, totalnums totalNums, winnums winNums, lostnums lostNums, drawnums drawNums " +
				"FROM chess_user WHERE account = ? AND password = ?";
		return select(User.class, sql, account, password);
	}

	/**
	 * Registers a new user into the database.
	 * @param user the User object containing the user's details
	 * @return the number of rows affected by the insert operation
	 */
	public int saveUser(User user) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO chess_user VALUES(?,?,?,?,?,?,?,?,?)";
		return update(sql, null,user.getAccount(), user.getPassword(),user.getRegTime(),
				100, 0, 0, 0, 0);
	}

	// Additional methods to handle updates related to game outcomes
	public int saveWinChessByAccount(String account) {
		int newScore = queryScoreByAccount(account) + 1;
		int newTotalNums = queryTotalNumsByAccount(account) + 1;
		int newWinNums = queryWinNumsByAccount(account) + 1;
		String sql = "UPDATE chess_user SET score = ?, totalnums = ?, winnums = ? WHERE account = ?";
		return update(sql, newScore, newTotalNums, newWinNums, account);
	}

	public int saveLostChessByAccount(String account) {
		int newScore = queryScoreByAccount(account) - 1;
		int newTotalNums = queryTotalNumsByAccount(account) + 1;
		int newLostNums = queryLostNumsByAccount(account) + 1;
		String sql = "UPDATE chess_user SET score = ?, totalnums = ?, lostnums = ? WHERE account = ?";
		return update(sql, newScore, newTotalNums, newLostNums, account);
	}

	public int saveDrawChessByAccount(String account) {
		int newTotalNums = queryTotalNumsByAccount(account) + 1;
		int newDrawNums = queryDrawNumsByAccount(account) + 1;
		String sql = "UPDATE chess_user SET totalnums = ?, drawnums = ? WHERE account = ?";
		return update(sql, newTotalNums, newDrawNums, account);
	}

	// Methods for querying user stats
	public int queryScoreByAccount(String account) {
		String sql = "SELECT score FROM chess_user WHERE account = ?";
		return select(User.class, sql, account).getScore();
	}

	public int queryTotalNumsByAccount(String account) {
		String sql = "SELECT totalnums totalNums FROM chess_user WHERE account = ?";
		return select(User.class, sql, account).getTotalNums();
	}

	public int queryWinNumsByAccount(String account) {
		String sql = "SELECT winnums winNums FROM chess_user WHERE account = ?";
		return select(User.class, sql, account).getWinNums();
	}

	public int queryLostNumsByAccount(String account) {
		String sql = "SELECT lostnums lostNums FROM chess_user WHERE account = ?";
		return select(User.class, sql, account).getLostNums();
	}

	public int queryDrawNumsByAccount(String account) {
		String sql = "SELECT drawnums drawNums FROM chess_user WHERE account = ?";
		return select(User.class, sql, account).getDrawNums();
	}
}
