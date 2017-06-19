package mrriegel.furnus.item;

import mrriegel.furnus.tile.TileDevice;
import mrriegel.furnus.tile.TileDevice.Upgrade;
import mrriegel.furnus.util.CreativeTab;
import mrriegel.limelib.item.CommonSubtypeItem;
import net.minecraft.item.ItemStack;

public class ItemUpgrade extends CommonSubtypeItem{

	public ItemUpgrade() {
		super("upgrade", TileDevice.Upgrade.values().length);
		setCreativeTab(CreativeTab.tab);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return Upgrade.values()[stack.getItemDamage()].maxStacksize;
	}

}
