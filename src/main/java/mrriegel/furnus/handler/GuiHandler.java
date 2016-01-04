package mrriegel.furnus.handler;

import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.gui.FurnusContainer;
import mrriegel.furnus.gui.FurnusGUI;
import mrriegel.furnus.gui.IOFGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public static final int FURNUS = 0;
	public static final int IO = 33;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == FURNUS)
			return new FurnusContainer(player.inventory,
					(TileFurnus) world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == FURNUS)
			return new FurnusGUI(new FurnusContainer(player.inventory,
					(TileFurnus) world.getTileEntity(new BlockPos(x, y, z))));
		else if(ID == IO)
			return new IOFGui((TileFurnus) world.getTileEntity(new BlockPos(x, y, z)), ID);
		return null;
	}

}
