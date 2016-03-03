package mrriegel.furnus.item;

import java.util.List;

import mrriegel.furnus.CreativeTab;
import mrriegel.furnus.Furnus;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDust extends Item {
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public enum Dust {
		IRON, GOLD;
	}

	public ItemDust() {
		super();
		this.setCreativeTab(CreativeTab.tab1);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(Furnus.MODID + ":dust");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		icons = new IIcon[Dust.values().length];
		for (int i = 0; i < Dust.values().length; i++) {
			this.icons[i] = reg.registerIcon(Furnus.MODID + ":dust_" + Dust.values()[i].toString().toLowerCase());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		if (meta > Dust.values().length - 1)
			meta = 0;

		return this.icons[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < Dust.values().length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_" + Dust.values()[stack.getItemDamage()].toString().toLowerCase();
	}

}
