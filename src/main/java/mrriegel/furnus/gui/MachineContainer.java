package mrriegel.furnus.gui;

import java.util.List;

import mrriegel.furnus.block.AbstractMachine;
import mrriegel.furnus.item.ModItems;
import mrriegel.limelib.gui.CommonContainerTileInventory;
import mrriegel.limelib.gui.slot.SlotFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.NonNullList;

import com.google.common.collect.Lists;

public class MachineContainer extends CommonContainerTileInventory<AbstractMachine> {
	EntityPlayer player;
	int startSlot;
	int tileSlots;
	boolean burn;

	public MachineContainer(InventoryPlayer inventory, AbstractMachine tileEntity) {
		super(inventory, tileEntity);
		startSlot = getTile().getSlots();
		burn = getTile().isBurning();
	}

	@Override
	protected void modifyInvs() {
		super.modifyInvs();
		player = invPlayer.player;
	}

	@Override
	protected void initSlots() {
		inventoryItemStacks = NonNullList.create();
		inventorySlots = Lists.newArrayList();
		switch (getTile().getSlots()) {
		case 0:
			this.addSlotToContainer(new InputSlot(getTile(), 0, 20, 48));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 3, 77, 48));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 6, 107, 48));
			break;
		case 1:
			this.addSlotToContainer(new InputSlot(getTile(), 0, 20, 48 - 13));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 3, 77, 48 - 13));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 6, 107, 48 - 13));
			this.addSlotToContainer(new InputSlot(getTile(), 1, 20, 48 + 14));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 4, 77, 48 + 14));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 7, 107, 48 + 14));
			break;
		case 2:
			this.addSlotToContainer(new InputSlot(getTile(), 0, 20, 48 - 27));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 3, 77, 48 - 27));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 6, 107, 48 - 27));
			this.addSlotToContainer(new InputSlot(getTile(), 1, 20, 48));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 4, 77, 48));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 7, 107, 48));
			this.addSlotToContainer(new InputSlot(getTile(), 2, 20, 48 + 27));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 5, 77, 48 + 27));
			this.addSlotToContainer(new OutputSlot(player, getTile(), 8, 107, 48 + 27));
			break;
		}
		this.addSlotToContainer(new SlotFilter(getTile(), 9, 20, 21 + 27 * 3, s -> TileEntityFurnace.isItemFuel(s)));

		int index = 10;
		for (int i = 0; i < 5; i++) {
			this.addSlotToContainer(new UpgradeSlot(this, player, getTile(), index++, 152, 12 + i * 18));
		}
		tileSlots = inventorySlots.size();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + 47 + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142 + 47));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (startSlot != getTile().getSlots() || burn != getTile().isBurning()) {
			startSlot = getTile().getSlots();
			burn = getTile().isBurning();
			ItemStack save = ItemStack.EMPTY;
			if (!player.inventory.getItemStack().isEmpty()) {
				save = player.inventory.getItemStack().copy();
				player.inventory.setItemStack(ItemStack.EMPTY);
			}
			if (!player.world.isRemote)
				getTile().openGUI((EntityPlayerMP) player);
			if (!save.isEmpty() && !player.world.isRemote) {
				player.inventory.setItemStack(save);
				((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(-1, 0, save));
			}
		}
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
			if (s.isEmpty())
				continue;
			if (s.getItemDamage() == meta)
				return i;
		}
		return -1;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotIndex <= tileSlots - 1) {
				if (!this.mergeItemStack(itemstack1, tileSlots, tileSlots + player.inventory.mainInventory.size(), true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			} else {
				boolean merged = false;
				if (itemstack1.getItem() == ModItems.upgrade) {
					if (getSlotWithUpgrade(itemstack1.getItemDamage()) == -1) {
						if (this.mergeItemStack(itemstack1, getUpgradeSlots()[0], getUpgradeSlots()[getUpgradeSlots().length - 1] + 1, false))
							merged = true;
					} else {
						if (this.mergeItemStack(itemstack1, getSlotWithUpgrade(itemstack1.getItemDamage()), getSlotWithUpgrade(itemstack1.getItemDamage()) + 1, false))
							merged = true;
					}
				}
				if (!merged && TileEntityFurnace.isItemFuel(itemstack1)) {
					if (this.mergeItemStack(itemstack1, getFuelputSlot(), getFuelputSlot() + 1, false)) {
						merged = true;
					}
				}
				boolean canProcess = !getTile().getResult(itemstack1).isEmpty();
				if (!merged && canProcess) {
					for (int i = 0; i < getInputSlots().length; i++)
						if (this.mergeItemStack(itemstack1, getInputSlots()[i], getInputSlots()[i] + 1, false)) {
							merged = true;
							break;
						}
				}
				if (!merged)
					return ItemStack.EMPTY;

			}
			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}

	@Override
	protected List<Area> allowedSlots(ItemStack stack, IInventory inv, int index) {
		return null;
	}
}
