package mrriegel.furnus;

import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.oredict.OreDictionary;

public class InventoryHelper {

	/** nicked from reika */
	public static boolean insert(IInventory inv, ItemStack is, boolean simulate) {
		ItemStack stack = is.copy();
		Integer[] slots = getSlotsWith(inv, stack.getItem(), stack.getItemDamage());
		int empty = findEmptySlot(inv);

		int max = Math.min(inv.getInventoryStackLimit(), stack.getMaxStackSize());

		int addable = 0;
		ArrayList<Integer> validslots = new ArrayList();
		for (int i = 0; i < slots.length && stack.stackSize > 0; i++) {
			int slot = slots[i];
			if (inv.isItemValidForSlot(slot, stack)) {
				ItemStack in = inv.getStackInSlot(slot);
				if (areStacksEqual(stack, in, true)) {
					int space = Math.min(max - in.stackSize, stack.stackSize);
					addable += space;
					validslots.add(slot);
				}
			}
		}
		if (empty != -1)
			addable += stack.getMaxStackSize();

		if (addable < stack.stackSize)
			return false;
		for (int i = 0; i < validslots.size() && stack.stackSize > 0; i++) {
			int slot = validslots.get(i);
			ItemStack in = inv.getStackInSlot(slot);
			int space = Math.min(max - in.stackSize, stack.stackSize);
			if (!simulate)
				in.stackSize += space;
			stack.stackSize -= space;
		}
		if (stack.stackSize <= 0)
			return true;
		if (empty != -1) {
			if (!simulate)
				inv.setInventorySlotContents(empty, stack.copy());
			return true;
		}
		return false;
	}

	/** nicked from reika */
	public static int addToInventoryWithLeftover(ItemStack stack, IInventory inventory,
			boolean simulate) {
		int left = stack.stackSize;
		int max = Math.min(inventory.getInventoryStackLimit(), stack.getMaxStackSize());
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack in = inventory.getStackInSlot(i);
			if (!inventory.isItemValidForSlot(i, stack))
				continue;
			if (in != null && stack.isItemEqual(in) && ItemStack.areItemStackTagsEqual(stack, in)) {
				int space = max - in.stackSize;
				int add = Math.min(space, stack.stackSize);
				if (add > 0) {
					if (!simulate)
						in.stackSize += add;
					left -= add;
					if (left <= 0)
						return 0;
				}
			}
		}
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack in = inventory.getStackInSlot(i);
			if (!inventory.isItemValidForSlot(i, stack))
				continue;
			if (in == null) {
				int add = Math.min(max, left);
				if (!simulate)
					inventory.setInventorySlotContents(i, copyStack(stack, add));
				left -= add;
				if (left <= 0)
					return 0;
			}
		}
		return left;
	}

	/** nicked from reika */
	public static int addToSidedInventoryWithLeftover(ItemStack stack, ISidedInventory inventory,
			EnumFacing side, boolean simulate) {
		int left = stack.stackSize;
		int max = Math.min(inventory.getInventoryStackLimit(), stack.getMaxStackSize());
		for (int i : inventory.getSlotsForFace(side)) {
			ItemStack in = inventory.getStackInSlot(i);
			if (!inventory.isItemValidForSlot(i, stack) || !inventory.canInsertItem(i, stack, side))
				continue;
			if (in != null && stack.isItemEqual(in) && ItemStack.areItemStackTagsEqual(stack, in)) {
				int space = max - in.stackSize;
				int add = Math.min(space, stack.stackSize);
				if (add > 0) {
					if (!simulate)
						in.stackSize += add;
					left -= add;
					if (left <= 0)
						return 0;
				}
			}
		}
		for (int i : inventory.getSlotsForFace(side)) {
			ItemStack in = inventory.getStackInSlot(i);
			if (!inventory.isItemValidForSlot(i, stack) || !inventory.canInsertItem(i, stack, side))
				continue;
			if (in == null) {
				int add = Math.min(max, left);
				if (!simulate)
					inventory.setInventorySlotContents(i, copyStack(stack, add));
				left -= add;
				if (left <= 0)
					return 0;
			}
		}
		return left;
	}

	private static int findEmptySlot(IInventory inv) {
		for (int i = 0; i < inv.getSizeInventory() - ((inv instanceof InventoryPlayer) ? 4 : 0); i++) {
			ItemStack is = inv.getStackInSlot(i);
			if (is == null)
				return i;
			if (is.stackSize <= 0) {
				is = null;
				return i;
			}
		}
		return -1;
	}

	public static ItemStack copyStack(ItemStack stack, int size) {
		if (stack == null)
			return null;
		ItemStack tmp = stack.copy();
		tmp.stackSize = size;
		return tmp;
	}

	public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2, boolean tags) {
		if (stack1 == null && stack2 == null)
			return true;
		if (stack1 == null || stack2 == null)
			return false;
		if (!tags)
			return stack1.isItemEqual(stack2);
		else
			return stack1.isItemEqual(stack2)
					&& ((stack1.getTagCompound() == null && stack2.getTagCompound() == null) || (stack1
							.getTagCompound() != null && stack2.getTagCompound() != null && stack1
							.getTagCompound().equals(stack2.getTagCompound())));
	}

	public static boolean decrStackSize(IInventory inv, int slot, int num) {
		ItemStack stack = inv.getStackInSlot(slot).copy();
		if (stack == null || stack.stackSize < num)
			return false;
		if (stack.stackSize == num) {
			inv.setInventorySlotContents(slot, null);
			inv.markDirty();
			return true;
		} else {
			stack.stackSize -= num;
			inv.setInventorySlotContents(slot, stack);
			inv.markDirty();
			return true;
		}
	}

	public static boolean incrStackSize(IInventory inv, int slot, int num) {
		ItemStack stack = inv.getStackInSlot(slot);
		if (stack == null || stack.stackSize + num > stack.getMaxStackSize())
			return false;
		stack.stackSize += num;
		inv.setInventorySlotContents(slot, stack);
		inv.markDirty();
		return true;
	}

	public static boolean incrStackInSlot(IInventory inv, int slot, ItemStack stack) {
		if (incrStackSize(inv, slot, stack.stackSize))
			return true;
		if (canFillSlot(inv, slot, stack)) {
			inv.setInventorySlotContents(slot, stack);
			return true;
		}
		return false;
		// if (inv.getStackInSlot(slot) != null && canFillSlot(inv, slot,
		// stack))
		// return incrStackSize(inv, slot, stack.stackSize);
		// else if (inv.getStackInSlot(slot) != null)
		// return false;
		// else if (canFillSlot(inv, slot, stack)) {
		// inv.setInventorySlotContents(slot, stack);
		// return true;
		// }
		// return false;

	}

	public static boolean canFillSlot(IInventory inv, int slot, ItemStack stack) {
		if (inv.getStackInSlot(slot) == null)
			return true;
		if (!areStacksEqual(stack, inv.getStackInSlot(slot), true))
			return false;
		if (inv.getStackInSlot(slot).stackSize + stack.stackSize > stack.getMaxStackSize())
			return false;
		return true;
	}

	public static Integer[] getSlotsWith(IInventory inv, Item item, int meta) {
		ArrayList<Integer> ar = new ArrayList<Integer>();
		for (int i = 0; i < inv.getSizeInventory() - ((inv instanceof InventoryPlayer) ? 4 : 0); ++i) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null && stack.getItem().equals(item)
					&& (stack.getItemDamage() == meta || meta == OreDictionary.WILDCARD_VALUE)) {
				ar.add(i);
			}
		}
		return ar.toArray(new Integer[ar.size()]);
	}

	public static Integer[] getSlotsWith(ItemStack[] inv, Item item, int meta) {
		ArrayList<Integer> ar = new ArrayList<Integer>();
		for (int i = 0; i < inv.length; ++i) {
			ItemStack stack = inv[i];
			if (stack != null && stack.getItem().equals(item)
					&& (stack.getItemDamage() == meta || meta == OreDictionary.WILDCARD_VALUE)) {
				ar.add(i);
			}
		}
		return ar.toArray(new Integer[ar.size()]);
	}
}
