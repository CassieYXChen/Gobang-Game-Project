package GroupF2.gobang.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represents the user entity with attributes for user identification,
 * account management, and game statistics. This class includes details such as user ID,
 * account information, password, registration time, and various game performance metrics.
 * This class implements Serializable to allow user instances to be serialized for storage
 * or transmission.
 *
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1L;  // Ensures version compatibility during serialization.

	private int id;            // Unique identifier for the user.
	private String account;    // Username or account ID of the user.
	private String password;   // Password for the user account.
	private Timestamp regTime; // Timestamp of when the user registered.
	private int score;         // Total score accumulated by the user.
	private int totalNums;     // Total number of games played.
	private int winNums;       // Number of games won by the user.
	private int lostNums;      // Number of games lost by the user.
	private int drawNums;      // Number of games that ended in a draw.

	/**
	 * Returns the user's account name.
	 * @return the account name as a String.
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Sets the user's account name.
	 * @param account the new account name as a String.
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * Returns the user's password.
	 * @return the password as a String.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the user's password.
	 * @param password the new password as a String.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the registration time of the user.
	 * @return the registration time as a Timestamp.
	 */
	public Timestamp getRegTime() {
		return regTime;
	}

	/**
	 * Sets the registration time of the user.
	 * @param regTime the new registration time as a Timestamp.
	 */
	public void setRegTime(Timestamp regTime) {
		this.regTime = regTime;
	}

	/**
	 * Returns the total score of the user.
	 * @return the score as an integer.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Returns the total number of games played by the user.
	 * @return the total number of games as an integer.
	 */
	public int getTotalNums() {
		return totalNums;
	}

	/**
	 * Returns the number of games won by the user.
	 * @return the number of wins as an integer.
	 */
	public int getWinNums() {
		return winNums;
	}

	/**
	 * Returns the number of games lost by the user.
	 * @return the number of losses as an integer.
	 */
	public int getLostNums() {
		return lostNums;
	}

	/**
	 * Returns the number of games that ended in a draw for the user.
	 * @return the number of draws as an integer.
	 */
	public int getDrawNums() {
		return drawNums;
	}

	/**
	 * Provides a string representation of this User instance, useful for logging and debugging.
	 * Includes details such as user ID, account, password, registration time, score, and game statistics.
	 * @return a string representation of the User object.
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", password=" + password + ", regTime=" + regTime
				+ ", score=" + score + ", totalNums=" + totalNums + ", winNums=" + winNums + ", lostNums=" + lostNums
				+ ", drawNums=" + drawNums + "]";
	}
}
