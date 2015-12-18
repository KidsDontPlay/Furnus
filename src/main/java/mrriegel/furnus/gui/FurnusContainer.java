package mrriegel.furnus.gui;

import mrriegel.furnus.block.TileFurnus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import cpw.mods.fml.common.IFuelHandler;

public class FurnusContainer extends Container {
	TileFurnus tile;

	public FurnusContainer(InventoryPlayer inventory, TileFurnus tileEntity) {
		tile = tileEntity;
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		ContainerFurnace d;
		IFuelHandler s;
		return true;
	}

	public TileFurnus getTile() {
		return tile;
	}

}
