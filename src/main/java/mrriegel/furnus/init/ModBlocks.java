package mrriegel.furnus.init;

import mrriegel.furnus.block.BlockDevice;
import mrriegel.furnus.tile.TileFurnus;
import mrriegel.furnus.tile.TilePulvus;
import mrriegel.limelib.block.CommonBlock;

public class ModBlocks {

	public static final CommonBlock furnus = new BlockDevice("furnus", TileFurnus.class);
	public static final CommonBlock pulvus = new BlockDevice("pulvus", TilePulvus.class);

	public static void init() {
		furnus.registerBlock();
		pulvus.registerBlock();
	}

	public static void initClient() {
		furnus.initModel();
		pulvus.initModel();
	}

}
