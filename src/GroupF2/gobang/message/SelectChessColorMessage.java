package GroupF2.gobang.message;

public class SelectChessColorMessage extends Message {
	private int color;

	// Black and white colors
	public static final int WHITE = 0;
	public static final int BLACK = 1;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}
