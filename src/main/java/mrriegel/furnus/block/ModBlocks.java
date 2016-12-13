package mrriegel.furnus.block;

import java.util.Map;

import mrriegel.furnus.Furnus;
import mrriegel.limelib.block.CommonBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(value = Furnus.MODID)
public class ModBlocks {
	public static final CommonBlock furnus = new BlockFurnus();
	public static final CommonBlock pulvus = new BlockPulvus();

	public static void init() {
		Map<String, Class<?>> teMappings = ObfuscationReflectionHelper.getPrivateValue(TileEntity.class, null, "field_" + "145855_i", "nameToClassMap");
		furnus.registerBlock();
		teMappings.put("tileFurnus", TileFurnus.class);
		pulvus.registerBlock();
		teMappings.put("tilePulvus", TilePulvus.class);
	}

}