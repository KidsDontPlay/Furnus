package mrriegel.furnus.handler;

import mrriegel.furnus.block.AbstractMachine;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.block.TilePulvus;
import mrriegel.furnus.gui.IOFGui;
import mrriegel.furnus.gui.MachineContainer;
import mrriegel.furnus.gui.MachineGUI;
import mrriegel.limelib.tile.CommonTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public static final int FURNUS = 1000, PULVUS = 1001;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		((CommonTile) world.getTileEntity(new BlockPos(x, y, z))).markForSync();
		if (ID == FURNUS)
			return new MachineContainer(player.inventory, (TileFurnus) world.getTileEntity(new BlockPos(x, y, z)));
		if (ID == PULVUS)
			return new MachineContainer(player.inventory, (TilePulvus) world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == FURNUS)
			return new MachineGUI(new MachineContainer(player.inventory, (TileFurnus) world.getTileEntity(new BlockPos(x, y, z))));
		if (ID == PULVUS)
			return new MachineGUI(new MachineContainer(player.inventory, (TilePulvus) world.getTileEntity(new BlockPos(x, y, z))));
		else
			return new IOFGui((AbstractMachine) world.getTileEntity(new BlockPos(x, y, z)), ID);
	}

}
