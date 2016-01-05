package mrriegel.furnus;

import mrriegel.furnus.block.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTab {
	public static CreativeTabs tab1 = new CreativeTabs(Furnus.MODID) {

		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(ModBlocks.furnus);
		}

		@Override
		public String getTranslatedTabLabel() {
			return Furnus.MODNAME;
		}
	};
}
