package mrriegel.furnus.item;

import mrriegel.furnus.init.ModConfig;
import mrriegel.furnus.util.CreativeTab;
import mrriegel.limelib.item.CommonSubtypeItem;

public class ItemDust extends CommonSubtypeItem {

	public ItemDust() {
		super("dust", 2);
		setCreativeTab(CreativeTab.tab);
	}

	@Override
	public void registerItem() {
		if (!ModConfig.dusts)
			return;
		super.registerItem();
	}

}
