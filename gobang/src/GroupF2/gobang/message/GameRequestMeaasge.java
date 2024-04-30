package GroupF2.gobang.message;

/**
 * Class for game request messages
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class GameRequestMeaasge extends Message{
	private String Account;
	private int requestType;

	// Types of game request messages: 1, 2, 3
	public static final int GAME_REQUEST = 1; // Game request
	public static final int GAME_AGREE = 2; // Agree to game
	public static final int GAME_REFUSE = 3; // Refuse game

	public String getAccount() {
		return Account;
	}
	public void setAccount(String account) {
		Account = account;
	}
	public int getRequestType() {
		return requestType;
	}
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
}
