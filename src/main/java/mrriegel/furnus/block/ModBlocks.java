package mrriegel.furnus.block;

import mrriegel.furnus.Furnus;
import mrriegel.limelib.block.CommonBlock;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(value = Furnus.MODID)
public class ModBlocks {
	public static final CommonBlock furnus = new BlockFurnus();
	public static final CommonBlock pulvus = new BlockPulvus();

	public static void init() {
		furnus.registerBlock();
		pulvus.registerBlock();
	}

}