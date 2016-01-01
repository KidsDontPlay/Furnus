package mrriegel.furnus;

import mrriegel.furnus.block.BlockFurnus;
import mrriegel.furnus.item.ItemUpgrade;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CraftingRecipes {

	public static void init() {
		GameRegistry.addShapedRecipe(new ItemStack(BlockFurnus.furnus), "ibi", "bob", "fbf", 'i',
				Items.iron_ingot, 'b', Blocks.brick_block, 'o', Blocks.furnace, 'f', Blocks.sand);
		GameRegistry.addShapedRecipe(new ItemStack(ItemUpgrade.upgrade, 1, 0), "cgc", "rir", "crc",
				'c', Items.clay_ball, 'i', Items.iron_ingot, 'g', Items.gold_nugget, 'r',
				Blocks.redstone_block);
		GameRegistry.addShapedRecipe(new ItemStack(ItemUpgrade.upgrade, 1, 1), "csc", "gig", "csc",
				'c', Items.clay_ball, 'i', Items.iron_ingot, 'g', Items.gold_ingot, 's',
				Blocks.snow);
		GameRegistry
				.addShapedRecipe(new ItemStack(ItemUpgrade.upgrade, 1, 2), "chc", " i ", "cpc",
						'c', Items.clay_ball, 'i', Items.iron_ingot, 'h', Blocks.hopper, 'p',
						Blocks.piston);
		GameRegistry.addShapedRecipe(new ItemStack(ItemUpgrade.upgrade, 1, 3), "chc", "hih", "chc",
				'c', Items.clay_ball, 'i', Items.iron_ingot, 'h', Items.diamond);
		GameRegistry.addShapedRecipe(new ItemStack(ItemUpgrade.upgrade, 1, 4), "cgc", "rir", "crc",
				'c', Items.clay_ball, 'i', Items.iron_ingot, 'g', Items.emerald, 'r',
				Blocks.lapis_block);
		GameRegistry.addShapedRecipe(new ItemStack(ItemUpgrade.upgrade, 1, 5), "cgc", "rir", "cgc",
				'c', Items.clay_ball, 'i', Items.iron_ingot, 'g', Items.slime_ball, 'r',
				Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(ItemUpgrade.upgrade, 1, 6), "cgc", "rir", "cgc",
				'c', Items.clay_ball, 'i', Items.iron_ingot, 'g', Blocks.coal_block, 'r',
				Items.blaze_powder);
	}

}
