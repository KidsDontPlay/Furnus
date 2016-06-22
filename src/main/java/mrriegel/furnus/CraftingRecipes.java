package mrriegel.furnus;

import mrriegel.furnus.block.ModBlocks;
import mrriegel.furnus.handler.ConfigHandler;
import mrriegel.furnus.item.ItemDust.Dust;
import mrriegel.furnus.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class CraftingRecipes {

	public static void init() {
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.furnus), "ibi", "bob", "fbf", 'i', Items.IRON_INGOT, 'b', Blocks.BRICK_BLOCK, 'o', Blocks.FURNACE, 'f', Blocks.SAND);
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.pulvus), "ibi", "bob", "fbf", 'i', Items.IRON_INGOT, 'b', Blocks.SANDSTONE, 'o', Blocks.FURNACE, 'f', Items.FLINT);
		if (ConfigHandler.speed)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 0), "cgc", "rir", "crc", 'c', Items.CLAY_BALL, 'i', Items.IRON_INGOT, 'g', Items.GOLD_NUGGET, 'r', Blocks.REDSTONE_BLOCK);
		if (ConfigHandler.effi)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 1), "csc", "gig", "csc", 'c', Items.CLAY_BALL, 'i', Items.IRON_INGOT, 'g', Items.GOLD_INGOT, 's', Blocks.SNOW);
		if (ConfigHandler.io)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 2), "chc", " i ", "cpc", 'c', Items.CLAY_BALL, 'i', Items.IRON_INGOT, 'h', Blocks.HOPPER, 'p', Blocks.PISTON);
		if (ConfigHandler.slot)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 3), "chc", "hih", "chc", 'c', Items.CLAY_BALL, 'i', Items.IRON_INGOT, 'h', Items.DIAMOND);
		if (ConfigHandler.bonus)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 4), "cgc", "rir", "crc", 'c', Items.CLAY_BALL, 'i', Items.IRON_INGOT, 'g', Items.EMERALD, 'r', Blocks.LAPIS_BLOCK);
		if (ConfigHandler.xp)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 5), "cgc", "rir", "cgc", 'c', Items.CLAY_BALL, 'i', Items.IRON_INGOT, 'g', Items.SLIME_BALL, 'r', Blocks.GLOWSTONE);
		if (ConfigHandler.eco)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 6), "cgc", "rir", "cgc", 'c', Items.CLAY_BALL, 'i', Items.IRON_INGOT, 'g', Blocks.COAL_BLOCK, 'r', Items.BLAZE_POWDER);
		if (ConfigHandler.rf)
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.upgrade, 1, 7), "cgc", "rir", "cgc", 'c', Items.CLAY_BALL, 'i', Items.IRON_INGOT, 'g', Items.GOLD_INGOT, 'r', Blocks.REDSTONE_BLOCK);
		if (ConfigHandler.dusts)
			for (int i = 0; i < Dust.values().length; i++) {
				Dust dus = Dust.values()[i];
				GameRegistry.addSmelting(new ItemStack(ModItems.dust, 1, i), OreDictionary.getOres("ingot" + dus.toString().substring(0, 1) + dus.toString().substring(1, dus.toString().length()).toLowerCase()).get(0), 0.3F);
			}
	}

}
