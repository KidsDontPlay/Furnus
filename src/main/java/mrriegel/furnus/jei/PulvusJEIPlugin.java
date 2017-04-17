package mrriegel.furnus.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.ModBlocks;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class PulvusJEIPlugin extends BlankModPlugin {

	@Override
	public void register(IModRegistry registry) {
		registry.addRecipeCategories(new PulvusCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeHandlers(new PulvusHandler());
		registry.addRecipes(PulvusHandler.getRecipes());
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.furnus), VanillaRecipeCategoryUid.SMELTING);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.pulvus), Furnus.MODID + ".pulvus");
	}

}
