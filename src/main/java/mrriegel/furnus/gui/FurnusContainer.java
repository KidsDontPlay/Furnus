package mrriegel.furnus.gui;

import java.util.ArrayList;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.TileFurnus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.Slot;
import cpw.mods.fml.common.IFuelHandler;

public class FurnusContainer extends Container {
	TileFurnus tile;
	EntityPlayer player;

	public FurnusContainer(InventoryPlayer inventory, TileFurnus tileEntity) {
		tile = tileEntity;
		player = inventory.player;
		initSlots();
	}

	void initSlots() {
		inventoryItemStacks = new ArrayList();
		inventorySlots = new ArrayList();
		this.addSlotToContainer(new InputSlot(player, tile, 0, 20, 21));
		this.addSlotToContainer(new OutputSlot(player, tile, 3, 77, 21));
		this.addSlotToContainer(new OutputSlot(player, tile, 6, 107, 21));

		if (tile.getSlot() > 0) {
			this.addSlotToContainer(new InputSlot(player, tile, 1, 20, 21 + 27));
			this.addSlotToContainer(new OutputSlot(player, tile, 4, 77, 21 + 27));
			this.addSlotToContainer(new OutputSlot(player, tile, 7, 107,
					21 + 27));
		}
		if (tile.getSlot() > 1) {
			this.addSlotToContainer(new InputSlot(player, tile, 2, 20, 21 + 54));
			this.addSlotToContainer(new OutputSlot(player, tile, 5, 77, 21 + 54));
			this.addSlotToContainer(new OutputSlot(player, tile, 8, 107,
					21 + 54));
		}

		this.addSlotToContainer(new FuelSlot(tile, 9, 20, 21 + 27 * 3));
		int index = 10;
		for (int i = 0; i < 5; i++) {
			this.addSlotToContainer(new UpgradeSlot(player, tile, index++, 152,
					12 + i * 18));
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9
						+ 9, 8 + j * 18, 84 + 47 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18,
					142 + 47));
		}
	}

	// public void updateSlots() {
	// System.out.println("change+ " + tile.getSlot());
	// if (tile.getSlot() < 2) {
	// player.openGui(Furnus.instance, 0, player.worldObj, tile.xCoord,
	// tile.yCoord, tile.zCoord);
	// // for (int i = 0; i < inventorySlots.size(); i++) {
	// // if (inventorySlots.get(i) instanceof InputSlot
	// // && ((InputSlot) inventorySlots.get(i))
	// // .equals(new InputSlot(null, tile, 1000, 20,
	// // 21 + 54)))
	// // inventorySlots.set(i, null);
	// // if (inventorySlots.get(i) instanceof OutputSlot
	// // && (((OutputSlot) inventorySlots.get(i))
	// // .equals(new OutputSlot(null, tile, 1000, 77,
	// // 21 + 54)) || ((OutputSlot) inventorySlots
	// // .get(i)).equals(new OutputSlot(null, tile,
	// // 1000, 107, 21 + 54))))
	// // inventorySlots.set(i, null);
	// // // inventorySlots.remove(new OutputSlot(null, tile, 1000, 77,
	// // // 21 + 54));
	// // // inventorySlots.remove(new OutputSlot(null, tile, 1000, 107,
	// // // 21 + 54));
	// // }
	// }
	// if (tile.getSlot() < 1) {
	// // boolean x = inventorySlots.remove(new InputSlot(null, tile, 1000,
	// // 20, 21 + 27));
	// // inventorySlots
	// // .remove(new OutputSlot(null, tile, 1000, 77, 21 + 27));
	// // inventorySlots
	// // .remove(new OutputSlot(null, tile, 1000, 107, 21 + 27));
	// // // System.out.println("x: " + x);
	// }
	// }

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}

	public TileFurnus getTile() {
		return tile;
	}

}
