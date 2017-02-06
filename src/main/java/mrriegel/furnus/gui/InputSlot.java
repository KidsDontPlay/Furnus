package mrriegel.furnus.gui;

import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.block.TilePulvus;
import mrriegel.furnus.handler.CrunchHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class InputSlot extends Slot {

	public InputSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
	}

	@Override
	public boolean isItemValid(ItemStack p_75214_1_) {
		if (inventory instanceof TileFurnus)
			return !FurnaceRecipes.instance().getSmeltingResult(p_75214_1_).isEmpty();
		if (inventory instanceof TilePulvus)
			return !CrunchHandler.instance().getResult(p_75214_1_).isEmpty();
		return false;
	}
}
