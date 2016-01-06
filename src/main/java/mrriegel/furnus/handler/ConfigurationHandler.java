package mrriegel.furnus.handler;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {
	public static Configuration config;

	public static int speedSize, effiSize, bonusSize, xpSize, speedMulti;
	public static double effiMulti, bonusMulti, xpMulti, bonusFuelMulti, speedFuelMulti;
	public static boolean dusts;
	public static String[] blacklistDusts;

	public static void refreshConfig() {

		speedSize = config.get("stacksize", "speedStackSize", 8, "Stacksize of Speed Upgrade")
				.getInt();
		effiSize = config.get("stacksize", "effiStackSize", 8, "Stacksize of Efficiency Upgrade")
				.getInt();
		bonusSize = config.get("stacksize", "bonusStackSize", 8, "Stacksize of Bonus Upgrade")
				.getInt();
		xpSize = config.get("stacksize", "xpStackSize", 8, "Stacksize of XP Upgrade").getInt();
		speedMulti = config.get("multiplier", "speedMulti", 1, "Multiplier of Speed Upgrade")
				.getInt();
		effiMulti = config.get("multiplier", "effiMulti", 1.5, "Multiplier of Efficiency Upgrade")
				.getDouble();
		bonusMulti = config.get("multiplier", "bonusMulti", .06,
				"Multiplier of Bonus Upgrade (more than 1.0 won't work)").getDouble();
		xpMulti = config.get("multiplier", "xpMulti", .25, "Multiplier of XP Upgrade").getDouble();
		bonusFuelMulti = config.get("multiplier", "bonusFuelMulti", 4.0,
				"Multiplier of Fuel Consumption of Bonus Upgrade").getDouble();
		speedFuelMulti = config.get("multiplier", "speedFuelMulti", 3.3,
				"Multiplier of Fuel Consumption of Speed Upgrade").getDouble();
		dusts = config.get("dust", "dusts", true, "Enable Dusts").getBoolean();
		blacklistDusts = config.get("dust", "blacklistDusts", new String[] { "dustCoal" },
				"Blacklist for dusts which should not be craftable in pulvus.").getStringList();
		if (config.hasChanged()) {
			config.save();
		}

	}

}