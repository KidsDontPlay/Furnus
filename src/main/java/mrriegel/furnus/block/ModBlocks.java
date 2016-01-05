package mrriegel.furnus.block;

import mrriegel.furnus.Furnus;
import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(value = Furnus.MODID)
public class ModBlocks {
	public static final Block furnus = new BlockFurnus();
	public static final Block pulvus = new BlockPulvus();

	public static void init() {
		GameRegistry.registerBlock(furnus, "furnus");
		GameRegistry.registerTileEntity(TileFurnus.class, "tileFurnus");
		GameRegistry.registerBlock(pulvus, "pulvus");
		GameRegistry.registerTileEntity(TilePulvus.class, "tilePulvus");
	}

}
