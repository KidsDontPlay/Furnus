package mrriegel.furnus.gui;

import java.util.ArrayList;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.item.ItemUpgrade;
import mrriegel.furnus.message.StackMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class FurnusContainer extends Container {
	TileFurnus tile;
	EntityPlayer player;
	int startSlot;
	int tileSlots;

	public FurnusContainer(InventoryPlayer inventory, TileFurnus tileEntity) {
		tile = tileEntity;
		player = inventory.player;
		initSlots();
		startSlot = tile.getSlots();
	}

	void initSlots() {
		inventoryItemStacks = new ArrayList();
		inventorySlots = new ArrayList();
		switch (tile.getSlots()) {
		case 0:
			this.addSlotToContainer(new InputSlot(tile, 0, 20, 48));
			this.addSlotToContainer(new OutputSlot(player, tile, 3, 77, 48));
			this.addSlotToContainer(new OutputSlot(player, tile, 6, 107, 48));
			break;
		case 1:
			this.addSlotToContainer(new InputSlot(tile, 0, 20, 48 - 13));
			this.addSlotToContainer(new OutputSlot(player, tile, 3, 77, 48 - 13));
			this.addSlotToContainer(new OutputSlot(player, tile, 6, 107, 48 - 13));
			this.addSlotToContainer(new InputSlot(tile, 1, 20, 48 + 14));
			this.addSlotToContainer(new OutputSlot(player, tile, 4, 77, 48 + 14));
			this.addSlotToContainer(new OutputSlot(player, tile, 7, 107, 48 + 14));
			break;
		case 2:
			this.addSlotToContainer(new InputSlot(tile, 0, 20, 48 - 27));
			this.addSlotToContainer(new OutputSlot(player, tile, 3, 77, 48 - 27));
			this.addSlotToContainer(new OutputSlot(player, tile, 6, 107, 48 - 27));
			this.addSlotToContainer(new InputSlot(tile, 1, 20, 48));
			this.addSlotToContainer(new OutputSlot(player, tile, 4, 77, 48));
			this.addSlotToContainer(new OutputSlot(player, tile, 7, 107, 48));
			this.addSlotToContainer(new InputSlot(tile, 2, 20, 48 + 27));
			this.addSlotToContainer(new OutputSlot(player, tile, 5, 77, 48 + 27));
			this.addSlotToContainer(new OutputSlot(player, tile, 8, 107, 48 + 27));
			break;
		}
		this.addSlotToContainer(new FuelSlot(tile, 9, 20, 21 + 27 * 3));

		int index = 10;
		for (int i = 0; i < 5; i++) {
			this.addSlotToContainer(new UpgradeSlot(this, player, tile, index++, 152, 12 + i * 18));
		}
		tileSlots = inventorySlots.size();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18,
						84 + 47 + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142 + 47));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (startSlot != tile.getSlots()) {
			startSlot = tile.getSlots();
			ItemStack save = null;
			if (player.inventory.getItemStack() != null) {
				save = player.inventory.getItemStack().copy();
				player.inventory.setItemStack(null);
			}
			player.openGui(Furnus.instance, 0, tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(),
					tile.getPos().getZ());
			if (save != null) {
				player.inventory.setItemStack(save);
				PacketHandler.INSTANCE.sendTo(new StackMessage(save), (EntityPlayerMP) player);
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}

	public TileFurnus getTile() {
		return tile;
	}

	int[] getInputSlots() {
		switch (tileSlots) {
		case 15:
			return new int[] { 0, 3, 6 };
		case 12:
			return new int[] { 0, 3 };
		case 9:
			return new int[] { 0 };
		}
		return null;
	}

	int[] getOutputSlots() {
		switch (tileSlots) {
		case 15:
			return new int[] { 1, 2, 4, 5, 7, 8 };
		case 12:
			return new int[] { 1, 2, 4, 5 };
		case 9:
			return new int[] { 1, 2 };
		}
		return null;
	}

	int getFuelputSlot() {
		switch (tileSlots) {
		case 15:
			return 9;
		case 12:
			return 6;
		case 9:
			return 3;
		}
		return -1;
	}

	int[] getUpgradeSlots() {
		switch (tileSlots) {
		case 15:
			return new int[] { 10, 11, 12, 13, 14 };
		case 12:
			return new int[] { 7, 8, 9, 10, 11 };
		case 9:
			return new int[] { 4, 5, 6, 7, 8 };
		}
		return null;
	}

	int getSlotWithUpgrade(int meta) {
		for (int i : getUpgradeSlots()) {
			ItemStack s = getSlot(i).getStack();
			if (s == null)
				continue;
			if (s.getItemDamage() == meta)
				return i;
		}
		return -1;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotIndex <= tileSlots - 1) {
				if (!this.mergeItemStack(itemstack1, tileSlots, tileSlots + 36, true)) {
					return null;
				}
				slot.onSlotChange(itemstack1, itemstack);
			} else {
				boolean merged = false;
				if (itemstack1.getItem() == ItemUpgrade.upgrade) {
					if (getSlotWithUpgrade(itemstack1.getItemDamage()) == -1) {
						if (this.mergeItemStack(itemstack1, getUpgradeSlots()[0],
								getUpgradeSlots()[getUpgradeSlots().length - 1] + 1, false))
							merged = true;
					} else {
						if (this.mergeItemStack(itemstack1,
								getSlotWithUpgrade(itemstack1.getItemDamage()),
								getSlotWithUpgrade(itemstack1.getItemDamage()) + 1, false))
							merged = true;
					}
				}
				if (!merged && TileEntityFurnace.isItemFuel(itemstack1)) {
					if (this.mergeItemStack(itemstack1, getFuelputSlot(), getFuelputSlot() + 1,
							false)) {
						merged = true;
					}
				}
				if (!merged && FurnaceRecipes.instance().getSmeltingResult(itemstack1) != null) {
					for (int i = 0; i < getInputSlots().length; i++)
						if (this.mergeItemStack(itemstack1, getInputSlots()[i],
								getInputSlots()[i] + 1, false)) {
							merged = true;
							break;
						}

				}
				if (!merged)
					return null;

			}
			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}
}
