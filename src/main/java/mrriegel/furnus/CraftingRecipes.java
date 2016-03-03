package mrriegel.furnus;

import mrriegel.furnus.block.ModBlocks;
import mrriegel.furnus.handler.ConfigurationHandler;
import mrriegel.furnus.item.ItemDust.Dust;
import mrriegel.furnus.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingRecipes {

	public static void init() {
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.furnus), "ibi", "bob", "fbf", 'i', Items.iron_ingot, 'b', Blocks.brick_block, 'o', Blocks.furnace, 'f', Blocks.sand);
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.pulvus), "ibi", "bob", "fbf", 'i', Items.iron_ingot, 'b', Blocks.sandstone, 'o', Blocks.furnace, 'f', Items.flint);
		if (ConfigurationHandler.speed)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 0), "cgc", "rir", "crc", 'c', Items.clay_ball, 'i', Items.iron_ingot, 'g', Items.gold_nugget, 'r', Blocks.redstone_block);
		if (ConfigurationHandler.effi)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 1), "csc", "gig", "csc", 'c', Items.clay_ball, 'i', Items.iron_ingot, 'g', Items.gold_ingot, 's', Blocks.snow);
		if (ConfigurationHandler.io)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 2), "chc", " i ", "cpc", 'c', Items.clay_ball, 'i', Items.iron_ingot, 'h', Blocks.hopper, 'p', Blocks.piston);
		if (ConfigurationHandler.slot)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 3), "chc", "hih", "chc", 'c', Items.clay_ball, 'i', Items.iron_ingot, 'h', Items.diamond);
		if (ConfigurationHandler.bonus)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 4), "cgc", "rir", "crc", 'c', Items.clay_ball, 'i', Items.iron_ingot, 'g', Items.emerald, 'r', Blocks.lapis_block);
		if (ConfigurationHandler.xp)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 5), "cgc", "rir", "cgc", 'c', Items.clay_ball, 'i', Items.iron_ingot, 'g', Items.slime_ball, 'r', Blocks.glowstone);
		if (ConfigurationHandler.eco)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 6), "cgc", "rir", "cgc", 'c', Items.clay_ball, 'i', Items.iron_ingot, 'g', Blocks.coal_block, 'r', Items.blaze_powder);
		if (ConfigurationHandler.dusts) {
			for (int i = 0; i < Dust.values().length; i++) {
				Dust dus = Dust.values()[i];
				GameRegistry.addSmelting(new ItemStack(ModItems.dust, 1, i), OreDictionary.getOres("ingot" + dus.toString().substring(0, 1) + dus.toString().substring(1, dus.toString().length()).toLowerCase()).get(0), 0.3F);
			}
		}
	}

}
