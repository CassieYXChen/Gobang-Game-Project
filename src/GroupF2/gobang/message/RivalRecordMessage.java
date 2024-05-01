package GroupF2.gobang.message;

import GroupF2.gobang.pojo.User;

/**
 * Class for messages that request the opponent's account information
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class RivalRecordMessage extends Message {
	// Takes value from two static constants
	private int rivalRecord;

	// Used to store opponent's information
	private User user;

	// Request message
	public static final int REQUEST = 1;
	// Response message
	public static final int RESPONSE = 2;

	public int getRivalRecord() {
		return rivalRecord;
	}

	public void setRivalRecord(int rivalRecord) {
		this.rivalRecord = rivalRecord;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

