package mrriegel.furnus.tile;

import mrriegel.furnus.Furnus;
import net.minecraft.entity.player.EntityPlayerMP;

public class TileFurnus extends TileDevice{

	public TileFurnus() {
		super();
	}

	@Override
	public boolean openGUI(EntityPlayerMP player) {
		player.openGui(Furnus.instance, 0, world, getX(), getY(), getZ());
		return true;
	}

}
