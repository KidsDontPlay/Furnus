package mrriegel.furnus.handler;

import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.gui.FurnusContainer;
import mrriegel.furnus.gui.FurnusGUI;
import mrriegel.furnus.gui.IOFGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (ID == 0)
			return new FurnusContainer(player.inventory,
					(TileFurnus) world.getTileEntity(x, y, z));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (ID == 0)
			return new FurnusGUI(new FurnusContainer(player.inventory,
					(TileFurnus) world.getTileEntity(x, y, z)));
		else
			return new IOFGui((TileFurnus) world.getTileEntity(x, y, z), ID);
	}

}
