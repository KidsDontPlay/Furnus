package mrriegel.furnus.tile;

import mrriegel.furnus.util.CrushHandler;
import net.minecraft.item.ItemStack;

public class TilePulvus extends TileDevice {

	@Override
	public ItemStack getResult(ItemStack input) {
		if (!results.containsKey(input))
			results.put(input, CrushHandler.instance().getResult(input));
		return results.get(input);
	}

}
