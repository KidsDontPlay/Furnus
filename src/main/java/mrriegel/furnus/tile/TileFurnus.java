package mrriegel.furnus.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class TileFurnus extends TileDevice {

	@Override
	public ItemStack getResult(ItemStack input) {
		return FurnaceRecipes.instance().getSmeltingResult(input);
	}

}
