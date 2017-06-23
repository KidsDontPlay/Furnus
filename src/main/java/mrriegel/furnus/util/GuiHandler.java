package mrriegel.furnus.util;

import mrriegel.furnus.gui.ContainerDevice;
import mrriegel.furnus.gui.GuiDevice;
import mrriegel.furnus.tile.TileDevice;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerDevice(player.inventory, (TileDevice) world.getTileEntity(new BlockPos(x, y, z)));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		ContainerDevice cd = new ContainerDevice(player.inventory, (TileDevice) world.getTileEntity(new BlockPos(x, y, z)));
		if (ID == 0)
			return new GuiDevice.Furnus(cd);
		else
			return new GuiDevice.Pulvus(cd);
	}

}
