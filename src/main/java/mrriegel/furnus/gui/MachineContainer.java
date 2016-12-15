package mrriegel.furnus.gui;

import java.util.List;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.AbstractMachine;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.block.TilePulvus;
import mrriegel.furnus.handler.CrunchHandler;
import mrriegel.furnus.handler.GuiHandler;
import mrriegel.furnus.item.ModItems;
import mrriegel.limelib.gui.CommonContainerTileInventory;
import mrriegel.limelib.gui.slot.SlotFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.ItemHandlerHelper;

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
		inventoryItemStacks = Lists.newArrayList();
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
			ItemStack save = null;
			if (player.inventory.getItemStack() != null) {
				save = player.inventory.getItemStack().copy();
				player.inventory.setItemStack(null);
			}
			Integer guiID = getTile() instanceof TileFurnus ? GuiHandler.FURNUS : getTile() instanceof TilePulvus ? GuiHandler.PULVUS : null;
			player.openGui(Furnus.instance, guiID, getTile().getWorld(), getTile().getX(), getTile().getY(), getTile().getZ());
			if (save != null && !player.worldObj.isRemote) {
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
		Slot slot = this.inventorySlots.get(slotIndex);

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
				boolean canProcess = getTile() instanceof TileFurnus ? FurnaceRecipes.instance().getSmeltingResult(itemstack1) != null : getTile() instanceof TilePulvus ? CrunchHandler.instance().getResult(itemstack1) != null : false;
				if (!merged && canProcess) {
					for (int i = 0; i < getInputSlots().length; i++)
						if (this.mergeItemStack(itemstack1, getInputSlots()[i], getInputSlots()[i] + 1, false)) {
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

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		boolean flag = false;
		int i = startIndex;

		if (reverseDirection) {
			i = endIndex - 1;
		}

		if (stack.isStackable()) {
			while (stack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
				Slot slot = this.inventorySlots.get(i);
				ItemStack itemstack = slot.getStack();

				if (itemstack != null && ItemHandlerHelper.canItemStacksStack(stack, itemstack)) {
					int j = itemstack.stackSize + stack.stackSize;
					int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());

					if (j <= maxSize) {
						stack.stackSize = 0;
						itemstack.stackSize = j;
						slot.onSlotChanged();
						flag = true;
					} else if (itemstack.stackSize < maxSize) {
						stack.stackSize -= maxSize - itemstack.stackSize;
						itemstack.stackSize = maxSize;
						slot.onSlotChanged();
						flag = true;
					}
				}

				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}

		if (stack.stackSize > 0) {
			if (reverseDirection) {
				i = endIndex - 1;
			} else {
				i = startIndex;
			}

			while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex) {
				Slot slot1 = this.inventorySlots.get(i);
				ItemStack itemstack1 = slot1.getStack();

				if (itemstack1 == null && slot1.isItemValid(stack)) // Forge: Make sure to respect isItemValid in the slot.
				{
					slot1.putStack(stack.copy());
					slot1.onSlotChanged();
					stack.stackSize = 0;
					flag = true;
					break;
				}

				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}

		return flag;
	}

	@Override
	protected List<Area> allowedSlots(ItemStack stack, IInventory inv, int index) {
		return null;
	}

//	@Override
//	public boolean canInteractWith(EntityPlayer playerIn) {
//		return getTile() != null && getTile().isUseableByPlayer(playerIn);
//	}
}
