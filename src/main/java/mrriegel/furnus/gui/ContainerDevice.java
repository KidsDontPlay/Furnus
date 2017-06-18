package mrriegel.furnus.gui;

import java.util.List;

import mrriegel.furnus.tile.TileDevice;
import mrriegel.limelib.gui.CommonContainerTileInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerDevice extends CommonContainerTileInventory<TileDevice>{

	public ContainerDevice(InventoryPlayer invPlayer, TileDevice tile) {
		super(invPlayer, tile);
	}

	@Override
	protected void initSlots() {
//		initPlayerSlots(0, 0);
	}

	@Override
	protected List<Area> allowedSlots(ItemStack stack, IInventory inv, int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
