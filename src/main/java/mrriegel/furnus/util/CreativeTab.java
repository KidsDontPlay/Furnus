package mrriegel.furnus.util;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.init.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab {

	public static final CreativeTabs tab = new CreativeTabs(Furnus.MODID) {

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
