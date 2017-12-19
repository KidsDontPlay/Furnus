package mrriegel.furnus.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class TileFurnus extends TileDevice {

	@Override
	public ItemStack getResult(ItemStack input) {
		if (!results.containsKey(input))
			results.put(input, FurnaceRecipes.instance().getSmeltingResult(input));
		return results.get(input);
	}

}
