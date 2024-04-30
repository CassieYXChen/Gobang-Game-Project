package GroupF2.gobang.message;

public class ReplayMessage extends Message {
	private int flag;

	// Represents the type of takeback message
	public static final int REPLAY_REQUEST = 1; // Takeback request
	public static final int REPLAY_AGRRE  = 2; // Agree to takeback
	public static final int REPLAY_REFUSE = 3; // Refuse takeback

	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}

