package mrriegel.furnus.item;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.handler.ConfigHandler;
import mrriegel.furnus.item.ItemDust.Dust;
import mrriegel.limelib.item.CommonItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;

@ObjectHolder(value = Furnus.MODID)
public class ModItems {

	public static final CommonItem upgrade = new ItemUpgrade();
	public static final CommonItem dust = new ItemDust();

	public static void init() {
		upgrade.registerItem();
		if (ConfigHandler.dusts) {
			dust.registerItem();
			for (int i = 0; i < Dust.values().length; i++) {
				Dust dus = Dust.values()[i];
				OreDictionary.registerOre("dust" + dus.toString().substring(0, 1) + dus.toString().substring(1, dus.toString().length()).toLowerCase(), new ItemStack(dust, 1, i));
			}
		}
	}
}