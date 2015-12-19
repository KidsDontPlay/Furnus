package mrriegel.furnus.gui;

import cpw.mods.fml.common.FMLCommonHandler;
import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.item.ItemUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class UpgradeSlot extends Slot {
	EntityPlayer player;
	FurnusContainer con;

	public UpgradeSlot(FurnusContainer furnusContainer, EntityPlayer player,
			IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_,
			int p_i1824_4_) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		this.player = player;
		con = furnusContainer;
	}

	@Override
	public boolean isItemValid(ItemStack p_75214_1_) {
		return p_75214_1_.getItem() == ItemUpgrade.upgrade
				&& in(p_75214_1_, getSlotIndex(), (TileFurnus) inventory);
	}

	public static boolean in(ItemStack stack, int slot, TileFurnus t) {
		for (int i = 10; i < 15; i++) {
			if (t.getStackInSlot(i) != null
					&& t.getStackInSlot(i).getItemDamage() == stack
							.getItemDamage() && i != slot)
				return false;
		}
		return true;
	}

	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		TileFurnus t = (TileFurnus) inventory;
		t.updateStats();
		// con.detectAndSendChanges();
		// System.out.println(player.openContainer.getClass());
		// if (getHasStack() && getStack().getItemDamage() == 3)
		// player.openGui(Furnus.instance, 0, t.getWorldObj(), t.yCoord,
		// t.yCoord, t.zCoord);
		// player.openContainer = new FurnusContainer(player.inventory, t);
		player.worldObj.markBlockForUpdate(t.xCoord, t.yCoord, t.zCoord);
		// for (int i = 0; i < t.INVSIZE; i++) {
		// System.out.println(FMLCommonHandler.instance().getEffectiveSide()
		// + " " + t.getStackInSlot(i));
		// }

	}
}
