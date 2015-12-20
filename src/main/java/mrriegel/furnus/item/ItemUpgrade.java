package mrriegel.furnus.item;

import java.util.List;

import mrriegel.furnus.CreativeTab;
import mrriegel.furnus.Furnus;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemUpgrade extends Item {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;;

	public ItemUpgrade() {
		super();
		this.setCreativeTab(CreativeTab.tab1);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(Furnus.MODID + ":upgrade");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister reg) {
		icons = new IIcon[7];
		for (int i = 0; i < 7; i++) {
			this.icons[i] = reg.registerIcon(Furnus.MODID + ":upgrade_" + i);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int meta) {
		if (meta > 6)
			meta = 0;

		return this.icons[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 7; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_" + stack.getItemDamage();
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 4;
		case 1:
			return 4;
		case 2:
			return 1;
		case 3:
			return 2;
		case 4:
			return 4;
		case 5:
			return 4;
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
