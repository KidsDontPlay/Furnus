package mrriegel.furnus;

import mrriegel.furnus.block.BlockFurnus;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTab {
	public static CreativeTabs tab1 = new CreativeTabs(Furnus.MODID) {

		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(BlockFurnus.furnus);
		}

		@Override
		public String getTranslatedTabLabel() {
			return Furnus.MODNAME;
		}
	};
}
