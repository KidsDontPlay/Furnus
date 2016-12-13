package mrriegel.furnus.item;

import mrriegel.furnus.CreativeTab;
import mrriegel.limelib.item.CommonSubtypeItem;
import net.minecraft.item.ItemStack;

public class ItemDust extends CommonSubtypeItem {

	public enum Dust {
		IRON, GOLD;
	}

	public ItemDust() {
		super("dust", Dust.values().length);
		this.setCreativeTab(CreativeTab.tab1);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_" + Dust.values()[stack.getItemDamage()].toString().toLowerCase();
	}

}