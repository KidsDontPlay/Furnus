package mrriegel.furnus.item;

import java.util.List;

import mrriegel.furnus.CreativeTab;
import mrriegel.furnus.Furnus;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpgrade extends Item {

	public ItemUpgrade() {
		super();
		this.setCreativeTab(CreativeTab.tab1);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(Furnus.MODID + ":upgrade");
	}

	// @Override
	// @SideOnly(Side.CLIENT)
	// public void registerIcons(IIconRegister reg) {
	// icons = new IIcon[7];
	// for (int i = 0; i < 7; i++) {
	// this.icons[i] = reg.registerIcon(Furnus.MODID + ":upgrade_" + i);
	// }
	// }
	//
	// @Override
	// @SideOnly(Side.CLIENT)
	// public IIcon getIconFromDamage(int meta) {
	// if (meta > 6)
	// meta = 0;
	//
	// return this.icons[meta];
	// }

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 7; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_,
			boolean p_77624_4_) {
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
		p_77624_3_.add(StatCollector.translateToLocal("item.furnus:upgrade_"
				+ p_77624_1_.getItemDamage() + ".tip"));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_" + stack.getItemDamage();
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 8;
		case 1:
			return 8;
		case 2:
			return 1;
		case 3:
			return 2;
		case 4:
			return 8;
		case 5:
			return 8;
		case 6:
			return 1;
		}
		return -1;
	}

	public static final Item upgrade = new ItemUpgrade();

	public static void init() {
		GameRegistry.registerItem(upgrade, "upgrade");
	}

}
