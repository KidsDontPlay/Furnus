package mrriegel.furnus.block;

import mrriegel.furnus.gui.UpgradeSlot;
import mrriegel.furnus.handler.ConfigHandler;
import mrriegel.furnus.handler.CrunchHandler;
import mrriegel.furnus.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class TilePulvus extends AbstractMachine {

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot >= 0 && slot <= 2)
			return CrunchHandler.instance().getResult(stack) != null;
		if (slot == 9)
			return TileEntityFurnace.isItemFuel(stack);
		if (slot >= 10)
			return stack.getItem() == ModItems.upgrade && UpgradeSlot.in(stack, slot, this);
		return false;
	}

	@Override
	protected void processItem(int slot) {
		if (this.canProcess(slot)) {
			ItemStack itemstack = CrunchHandler.instance().getResult(getStackInSlot(slot));

			if (getStackInSlot(slot + 3) == null) {
				setInventorySlotContents(slot + 3, itemstack.copy());
			} else if (getStackInSlot(slot + 3).isItemEqual(itemstack)) {
				getStackInSlot(slot + 3).stackSize += itemstack.stackSize;
			}
			boolean valid;
			try {
				valid = !getStackInSlot(slot).isItemEqual(FurnaceRecipes.instance().getSmeltingResult(getStackInSlot(slot + 3))) && !equalOreDict(getStackInSlot(slot), FurnaceRecipes.instance().getSmeltingResult(getStackInSlot(slot + 3)));
			} catch (NullPointerException e) {
				valid = true;
			}
			if ((worldObj.rand.nextInt(100) < bonus * 100 * ConfigHandler.bonusMulti)) {
				if (valid) {
					int ran = worldObj.rand.nextInt(itemstack.stackSize) + 1;
					if (getStackInSlot(slot + 6) == null) {
						setInventorySlotContents(slot + 6, CrunchHandler.resize(itemstack, ran));
					} else if (getStackInSlot(slot + 6).isItemEqual(itemstack)) {
						if (getStackInSlot(slot + 6).stackSize + itemstack.stackSize <= itemstack.getMaxStackSize())
							getStackInSlot(slot + 6).stackSize += ran;
						else
							getStackInSlot(slot + 6).stackSize = itemstack.getMaxStackSize();
					}
				}
			}

			--this.getStackInSlot(slot).stackSize;

			if (this.getStackInSlot(slot).stackSize <= 0) {
				setInventorySlotContents(slot, null);
			}
		}
	}

	@Override
	protected boolean canProcess(int slot) {
		if (getStackInSlot(slot) == null) {
			return false;
		} else {
			ItemStack itemstack = CrunchHandler.instance().getResult(getStackInSlot(slot));
			if (itemstack == null)
				return false;
			if (getStackInSlot(slot + 3) == null)
				return true;
			if (!getStackInSlot(slot + 3).isItemEqual(itemstack))
				return false;
			int result = getStackInSlot(slot + 3).stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit() && result <= getStackInSlot(slot + 3).getMaxStackSize();
		}
	}

	@Override
	protected boolean fit(ItemStack stack, int slot) {
		return getStackInSlot(slot + 3) == null || (CrunchHandler.instance().getResult(stack).isItemEqual(getStackInSlot(slot + 3)) && getStackInSlot(slot + 3).stackSize + CrunchHandler.instance().getResult(stack).stackSize <= getStackInSlot(slot + 3).getMaxStackSize());
	}

}