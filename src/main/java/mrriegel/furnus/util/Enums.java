package mrriegel.furnus.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;

public class Enums {
	public enum Upgrade {
		SPEED(8), EFFICIENCY(8), IO(1), SLOT(2), XP(8), ECO(1), ENERGY(1);
		public final int maxStacksize;
//		public final double effi;

		private Upgrade(int maxStacksize) {
			this.maxStacksize = maxStacksize;
		}
	}

	public enum Direction {
		BOTTOM, TOP, FRONT, BACK, RIGHT, LEFT;

		public EnumFacing face;

		private Direction() {
			face = EnumFacing.VALUES[this.ordinal()];

		}
	}

	public enum Mode {
		ENABLED(TextFormatting.GREEN.toString() + "O"), AUTO(TextFormatting.AQUA.toString() + "A"), DISABLED(TextFormatting.RED.toString() + "X");
		protected static Mode[] vals = values();

		public final String digit;

		private Mode(String digit) {
			this.digit = digit;
		}

		public Mode next() {
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}
}
