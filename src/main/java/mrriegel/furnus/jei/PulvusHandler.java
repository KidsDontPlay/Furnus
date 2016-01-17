package mrriegel.furnus.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mrriegel.furnus.Furnus;
import mrriegel.furnus.handler.CrunchHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PulvusHandler implements IRecipeHandler<PulvusWrapper> {

	@Override
	public Class<PulvusWrapper> getRecipeClass() {
		return PulvusWrapper.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return Furnus.MODID + ".pulvus";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(PulvusWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(PulvusWrapper recipe) {
		return recipe.getOutputs().size() > 0;
	}

	public static List<PulvusWrapper> getRecipes() {
		List<PulvusWrapper> recipes = new ArrayList<PulvusWrapper>();
		for (Entry<ItemStack, ItemStack> e : CrunchHandler.instance().crushingList.entrySet())
			recipes.add(new PulvusWrapper(e.getKey(), e.getValue()));
		Collections.sort(recipes, new Comparator<PulvusWrapper>() {

			@Override
			public int compare(PulvusWrapper o1, PulvusWrapper o2) {
				Integer id1 = Item.getIdFromItem(((ItemStack) o1.getOutputs().get(0)).getItem());
				Integer id2 = Item.getIdFromItem(((ItemStack) o2.getOutputs().get(0)).getItem());
				return id1.compareTo(id2);
			}

		});
		Collections.sort(recipes, new Comparator<PulvusWrapper>() {

			@Override
			public int compare(PulvusWrapper o1, PulvusWrapper o2) {
				Integer id1 = ((ItemStack) o1.getOutputs().get(0)).getItemDamage();
				Integer id2 = ((ItemStack) o2.getOutputs().get(0)).getItemDamage();
				return id1.compareTo(id2);
			}

		});
		return recipes;
	}

}
