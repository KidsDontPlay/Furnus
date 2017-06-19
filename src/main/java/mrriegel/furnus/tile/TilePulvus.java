package mrriegel.furnus.tile;

import mrriegel.furnus.util.CrushHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class TilePulvus extends TileDevice{

	@Override
	public boolean openGUI(EntityPlayerMP player) {
		return false;
	}

	@Override
	public ItemStack getResult(ItemStack input) {
		return CrushHandler.instance().getResult(input);
	}

}
