package GroupF2.gobang.message;

/**
 * New game message class
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class NewGameMessage extends Message {
	private String account;
	private int state;

	// Request
	public static final int REQUEST = 0;
	// Agree to new game
	public static final int OK = 1;
	// Reject new game
	public static final int NO = 2;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
