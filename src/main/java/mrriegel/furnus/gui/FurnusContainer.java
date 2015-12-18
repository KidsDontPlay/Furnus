package mrriegel.furnus.gui;

import java.util.ArrayList;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.TileFurnus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
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

		if (tile.getSlots() > 0) {
			this.addSlotToContainer(new InputSlot(player, tile, 1, 20, 21 + 27));
			this.addSlotToContainer(new OutputSlot(player, tile, 4, 77, 21 + 27));
			this.addSlotToContainer(new OutputSlot(player, tile, 7, 107,
					21 + 27));
		}
		if (tile.getSlots() > 1) {
			this.addSlotToContainer(new InputSlot(player, tile, 2, 20, 21 + 54));
			this.addSlotToContainer(new OutputSlot(player, tile, 5, 77, 21 + 54));
			this.addSlotToContainer(new OutputSlot(player, tile, 8, 107,
					21 + 54));
		}

		this.addSlotToContainer(new FuelSlot(tile, 9, 20, 21 + 27 * 3));

		int index = 10;
		for (int i = 0; i < 5; i++) {
			this.addSlotToContainer(new UpgradeSlot(this, player, tile,
					index++, 152, 12 + i * 18));
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
		detectAndSendChanges();
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}

	public TileFurnus getTile() {
		return tile;
	}

	@Override
	public ItemStack slotClick(int slot, int p_75144_2_, int p_75144_3_,
			EntityPlayer player) {
		tile.updateStats(player);
		detectAndSendChanges();
		initSlots();
		player.worldObj.markBlockForUpdate(tile.xCoord, tile.yCoord,
				tile.zCoord);

		return super.slotClick(slot, p_75144_2_, p_75144_3_, player);
	}

}
