package mrriegel.furnus.item;

import java.util.List;

import mrriegel.furnus.CreativeTab;
import mrriegel.furnus.handler.ConfigHandler;
import mrriegel.limelib.item.CommonSubtypeItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpgrade extends CommonSubtypeItem {

	public ItemUpgrade() {
		super("upgrade", 8);
		this.setCreativeTab(CreativeTab.tab1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		if (ConfigHandler.speed)
			list.add(new ItemStack(item, 1, 0));
		if (ConfigHandler.effi)
			list.add(new ItemStack(item, 1, 1));
		if (ConfigHandler.io)
			list.add(new ItemStack(item, 1, 2));
		if (ConfigHandler.slot)
			list.add(new ItemStack(item, 1, 3));
		if (ConfigHandler.bonus)
			list.add(new ItemStack(item, 1, 4));
		if (ConfigHandler.xp)
			list.add(new ItemStack(item, 1, 5));
		if (ConfigHandler.eco)
			list.add(new ItemStack(item, 1, 6));
		if (ConfigHandler.rf)
			list.add(new ItemStack(item, 1, 7));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		tooltip.add(I18n.format("item.furnus:upgrade_" + stack.getItemDamage() + ".tip"));
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return ConfigHandler.speedSize;
		case 1:
			return ConfigHandler.effiSize;
		case 2:
			return 1;
		case 3:
			return 2;
		case 4:
			return ConfigHandler.bonusSize;
		case 5:
			return ConfigHandler.xpSize;
		case 6:
			return 1;
		case 7:
			return 1;
		}
		return 0;
	}

}
