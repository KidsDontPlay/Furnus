package mrriegel.furnus.item;

import mrriegel.furnus.util.CreativeTab;
import mrriegel.limelib.item.CommonSubtypeItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemDust extends CommonSubtypeItem {

	public ItemDust() {
		super("dust", 2);
		setCreativeTab(CreativeTab.tab);
	}

	@Override
	public void registerItem() {
		super.registerItem();
		OreDictionary.registerOre("dustIron", new ItemStack(this, 1, 0));
		OreDictionary.registerOre("dustGold", new ItemStack(this, 1, 1));
	}

}
