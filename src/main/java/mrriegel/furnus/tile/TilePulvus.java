package mrriegel.furnus.tile;

import mrriegel.furnus.util.CrushHandler;
import net.minecraft.item.ItemStack;

public class TilePulvus extends TileDevice {

	@Override
	public ItemStack getResult(ItemStack input) {
		return CrushHandler.instance().getResult(input);
	}

}
