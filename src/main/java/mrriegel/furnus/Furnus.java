package mrriegel.furnus;

import java.io.File;

import mrriegel.furnus.block.BlockFurnus;
import mrriegel.furnus.handler.ConfigurationHandler;
import mrriegel.furnus.handler.GuiHandler;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.item.ItemUpgrade;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = Furnus.MODID, name = Furnus.MODNAME, version = Furnus.VERSION)
public class Furnus {
	public static final String MODID = "furnus";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "Furnus";

	@Instance(Furnus.MODID)
	public static Furnus instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File configFile = event.getSuggestedConfigurationFile();
		ConfigurationHandler.config = new Configuration(configFile);
		ConfigurationHandler.config.load();
		ConfigurationHandler.refreshConfig();
		PacketHandler.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		BlockFurnus.init();
		ItemUpgrade.init();
		CraftingRecipes.init();
	}
}
