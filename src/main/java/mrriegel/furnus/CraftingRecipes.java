package mrriegel.furnus;

import mrriegel.furnus.block.BlockFurnus;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingRecipes {

	public static void init() {
		GameRegistry.addShapedRecipe(new ItemStack(BlockFurnus.furnus), "ibi",
				"bob", "fbf", 'i', Items.iron_ingot, 'b', Blocks.brick_block,
				'o', Blocks.furnace, 'f', Blocks.sand);
	}

}
