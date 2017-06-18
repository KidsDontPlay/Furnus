package mrriegel.furnus.init;

import mrriegel.furnus.item.ItemDust;
import mrriegel.furnus.item.ItemUpgrade;
import mrriegel.limelib.item.CommonItem;

public class ModItems {

	public static final CommonItem dust = new ItemDust();
	public static final CommonItem upgrade=new ItemUpgrade();

	public static void init() {
		dust.registerItem();
		upgrade.registerItem();
	}

	public static void initClient() {
		dust.initModel();upgrade.initModel();
	}

}
