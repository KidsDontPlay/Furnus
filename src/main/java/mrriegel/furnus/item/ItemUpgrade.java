package mrriegel.furnus.item;

import mrriegel.furnus.tile.TileDevice;
import mrriegel.furnus.util.CreativeTab;
import mrriegel.limelib.item.CommonSubtypeItem;

public class ItemUpgrade extends CommonSubtypeItem{

	public ItemUpgrade() {
		super("upgrade", TileDevice.Upgrade.values().length);
		setCreativeTab(CreativeTab.tab);
	}

}
