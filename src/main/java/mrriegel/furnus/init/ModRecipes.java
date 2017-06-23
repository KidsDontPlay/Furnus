package mrriegel.furnus.init;

import mrriegel.limelib.helper.RecipeHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

	public static void init() {
		if (ModConfig.dusts) {
			GameRegistry.addSmelting(new ItemStack(ModItems.dust, 1, 0), new ItemStack(Items.IRON_INGOT), .1f);
			GameRegistry.addSmelting(new ItemStack(ModItems.dust, 1, 1), new ItemStack(Items.GOLD_INGOT), .1f);
		}
		RecipeHelper.addShapedOreRecipe(new ItemStack(ModBlocks.furnus), "ibi", "bfb", "#b#", 'i', "ingotIron", 'b', Blocks.BRICK_BLOCK, 'f', Blocks.FURNACE, '#', new ItemStack(Items.COAL));
		RecipeHelper.addShapedOreRecipe(new ItemStack(ModBlocks.pulvus), "ibi", "bfb", "#b#", 'i', "ingotIron", 'b', "sandstone", 'f', Blocks.FURNACE, '#', Items.FLINT);
		RecipeHelper.addShapedOreRecipe(new ItemStack(ModItems.upgrade, 1, 0), " g ", "rir", " R ", 'i', "ingotIron", 'g', "ingotGold", 'r', "dustRedstone", 'R', "blockRedstone");
		RecipeHelper.addShapedOreRecipe(new ItemStack(ModItems.upgrade, 1, 1), " g ", "rir", " R ", 'i', "ingotIron", 'g', "gemQuartz", 'r', "ingotGold", 'R', "glowstone");
		RecipeHelper.addShapedOreRecipe(new ItemStack(ModItems.upgrade, 1, 2), " p ", " i ", " h ", 'i', "ingotIron", 'p', Blocks.PISTON, 'h', Blocks.HOPPER);
		RecipeHelper.addShapedOreRecipe(new ItemStack(ModItems.upgrade, 1, 3), " g ", " i ", " g ", 'i', "ingotIron", 'g', "gemDiamond");
		RecipeHelper.addShapedOreRecipe(new ItemStack(ModItems.upgrade, 1, 4), " g ", "sis", " s ", 'i', "ingotIron", 'g', "dustGlowstone", 's', "slimeball");
		RecipeHelper.addShapedOreRecipe(new ItemStack(ModItems.upgrade, 1, 5), " g ", "sis", " G ", 'i', "ingotIron", 'g', Items.COAL, 's', Items.BLAZE_ROD, 'G', Blocks.COAL_BLOCK);
		RecipeHelper.addShapedOreRecipe(new ItemStack(ModItems.upgrade, 1, 6), " g ", "sis", " G ", 'i', "ingotIron", 'g', "dustGlowstone", 's', "blockRedstone", 'G', "ingotGold");
	}
}
