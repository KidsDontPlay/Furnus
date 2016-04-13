package mrriegel.furnus.item;

import java.util.List;

import mrriegel.furnus.CreativeTab;
import mrriegel.furnus.Furnus;
import mrriegel.furnus.handler.ConfigurationHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpgrade extends Item {

	public ItemUpgrade() {
		super();
		this.setCreativeTab(CreativeTab.tab1);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(Furnus.MODID + ":upgrade");
		// this.setRegistryName("upgrade");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		if (ConfigurationHandler.speed)
			list.add(new ItemStack(item, 1, 0));
		if (ConfigurationHandler.effi)
			list.add(new ItemStack(item, 1, 1));
		if (ConfigurationHandler.io)
			list.add(new ItemStack(item, 1, 2));
		if (ConfigurationHandler.slot)
			list.add(new ItemStack(item, 1, 3));
		if (ConfigurationHandler.bonus)
			list.add(new ItemStack(item, 1, 4));
		if (ConfigurationHandler.xp)
			list.add(new ItemStack(item, 1, 5));
		if (ConfigurationHandler.eco)
			list.add(new ItemStack(item, 1, 6));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
		p_77624_3_.add(I18n.translateToLocal("item.furnus:upgrade_" + p_77624_1_.getItemDamage() + ".tip"));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_" + stack.getItemDamage();
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return ConfigurationHandler.speedSize;
		case 1:
			return ConfigurationHandler.effiSize;
		case 2:
			return 1;
		case 3:
			return 2;
		case 4:
			return ConfigurationHandler.bonusSize;
		case 5:
			return ConfigurationHandler.xpSize;
		case 6:
			return 1;
		}
		return -1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}

}
