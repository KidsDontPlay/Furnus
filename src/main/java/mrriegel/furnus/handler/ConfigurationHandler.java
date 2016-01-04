package mrriegel.furnus.handler;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {
	public static Configuration config;

	public static int speedSize, effiSize, bonusSize, xpSize, speedMulti;
	public static double effiMulti, bonusMulti, xpMulti, bonusFuelMulti, speedFuelMulti;

	public static void refreshConfig() {

		speedSize = config.get("COMMON", "speedStackSize", 8, "Stacksize of Speed Upgrade")
				.getInt();
		effiSize = config.get("COMMON", "effiStackSize", 8, "Stacksize of Efficiency Upgrade")
				.getInt();
		bonusSize = config.get("COMMON", "bonusStackSize", 8, "Stacksize of Bonus Upgrade")
				.getInt();
		xpSize = config.get("COMMON", "xpStackSize", 8, "Stacksize of XP Upgrade").getInt();
		speedMulti = config.get("COMMON", "speedMulti", 1, "Multiplier of Speed Upgrade").getInt();
		effiMulti = config.get("COMMON", "effiMulti", 1.5, "Multiplier of Efficiency Upgrade")
				.getDouble();
		bonusMulti = config.get("COMMON", "bonusMulti", .1,
				"Multiplier of Bonus Upgrade (more than 1.0 won't work)").getDouble();
		xpMulti = config.get("COMMON", "xpMulti", .25, "Multiplier of XP Upgrade").getDouble();
		bonusFuelMulti = config.get("COMMON", "bonusFuelMulti", 4.0,
				"Multiplier of Fuel Consumption of Bonus Upgrade").getDouble();
		speedFuelMulti = config.get("COMMON", "speedFuelMulti", 3.3,
				"Multiplier of Fuel Consumption of Speed Upgrade").getDouble();
		if (config.hasChanged()) {
			config.save();
		}

	}

}