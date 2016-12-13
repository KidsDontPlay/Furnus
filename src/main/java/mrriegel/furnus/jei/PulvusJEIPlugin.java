package mrriegel.furnus.jei;

import mezz.jei.Internal;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.ModBlocks;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;

@JEIPlugin
public class PulvusJEIPlugin implements IModPlugin {

	@Override
	public void register(IModRegistry registry) {
		registry.addRecipeCategories(new PulvusCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeHandlers(new PulvusHandler());
		registry.addRecipes(PulvusHandler.getRecipes());
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.furnus), VanillaRecipeCategoryUid.SMELTING);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.pulvus), Furnus.MODID + ".pulvus");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime arg0) {
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {

	}

	public static void showCategory(String... category) {
		if (Internal.getRuntime() != null && Internal.getRuntime().getRecipesGui() != null) {
			Internal.getRuntime().getRecipesGui().showCategories(Lists.newArrayList(category));
		}
	}

}
