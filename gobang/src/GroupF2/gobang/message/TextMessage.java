package GroupF2.gobang.message;

/**
 * Chat text message sent to the opponent
 */
public class TextMessage extends Message {
	private String account;
	private String textMessage;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}
}
