package mrriegel.furnus;

import mrriegel.furnus.block.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab {
	public static CreativeTabs tab1 = new CreativeTabs(Furnus.MODID) {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModBlocks.furnus);
		}

		@Override
		public String getTranslatedTabLabel() {
			return Furnus.MODNAME;
		}
	};
}
