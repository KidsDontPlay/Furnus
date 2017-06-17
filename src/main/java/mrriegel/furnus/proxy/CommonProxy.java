package mrriegel.furnus.proxy;

import mrriegel.furnus.init.ModConfig;
import mrriegel.furnus.util.Recipe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		ModConfig.refreshConfig(event.getSuggestedConfigurationFile());
	}

	public void init(FMLInitializationEvent event) {
	}

	public void postInit(FMLPostInitializationEvent event) {
		for (Recipe r : ModConfig.recipes)
			r.register();
	}

}
