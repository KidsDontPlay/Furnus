package mrriegel.furnus.jei;

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

import com.google.common.collect.Lists;

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
		List<PulvusWrapper> recipes = Lists.newArrayList();
		for (Entry<ItemStack, ItemStack> e : CrunchHandler.instance().crushingList.entrySet())
			recipes.add(new PulvusWrapper(e.getKey(), e.getValue()));
		Collections.sort(recipes, new Comparator<PulvusWrapper>() {

			@Override
			public int compare(PulvusWrapper o1, PulvusWrapper o2) {
				Integer id1 = Item.getIdFromItem(o1.getOutputs().get(0).getItem());
				Integer id2 = Item.getIdFromItem(o2.getOutputs().get(0).getItem());
				if (id1.compareTo(id2) == 0)
					return new Integer(o1.getOutputs().get(0).getItemDamage()).compareTo(o2.getOutputs().get(0).getItemDamage());
				return id1.compareTo(id2);
			}

		});
		return recipes;
	}

	@Override
	public String getRecipeCategoryUid(PulvusWrapper recipe) {
		return Furnus.MODID + ".pulvus";
	}

}
