package mrriegel.furnus.handler;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {
	public static Configuration config;

	public static void refreshConfig() {
		if (config.hasChanged()) {
			config.save();
		}

	}

}