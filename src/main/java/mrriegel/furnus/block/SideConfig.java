package mrriegel.furnus.block;

public class SideConfig {
	private int[] slots;
	private boolean in, out, autoOut, autoIn;

	public SideConfig(int[] slots, boolean in, boolean out, boolean autoOut,
			boolean autoIn) {
		super();
		this.slots = slots;
		this.in = in;
		this.out = out;
		this.autoOut = autoOut;
		this.autoIn = autoIn;
	}

	public int[] getSlots() {
		return slots;
	}

	public void setSlots(int[] slots) {
		this.slots = slots;
	}

	public boolean isIn() {
		return in;
	}

	public void setIn(boolean in) {
		this.in = in;
	}

	public boolean isOut() {
		return out;
	}

	public void setOut(boolean out) {
		this.out = out;
	}

	public boolean isAutoOut() {
		return autoOut;
	}

	public void setAutoOut(boolean autoOut) {
		this.autoOut = autoOut;
	}

	public boolean isAutoIn() {
		return autoIn;
	}

	public void setAutoIn(boolean autoIn) {
		this.autoIn = autoIn;
	}

}
