package mrriegel.furnus;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import mrriegel.furnus.block.ModBlocks;
import mrriegel.furnus.handler.ConfigurationHandler;
import mrriegel.furnus.handler.CrunchHandler;
import mrriegel.furnus.handler.GuiHandler;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.item.ModItems;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
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
		ModBlocks.init();
		ModItems.init();
		CraftingRecipes.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		List<String> black = Arrays.asList(ConfigurationHandler.blacklistDusts);
		for (String ore : OreDictionary.getOreNames()) {
			if (ore.startsWith("ore")
					&& !OreDictionary.getOres("dust" + ore.substring(3)).isEmpty()
					&& !black.contains("dust" + ore.substring(3)))
				CrunchHandler.instance().addItemStack(
						OreDictionary.getOres(ore).get(0),
						CrunchHandler.resize(OreDictionary.getOres("dust" + ore.substring(3))
								.get(0), 2), 0.4F);
			else if (ore.startsWith("ingot")
					&& !OreDictionary.getOres("dust" + ore.substring(5)).isEmpty()
					&& !black.contains("dust" + ore.substring(5)))
				CrunchHandler.instance().addItemStack(OreDictionary.getOres(ore).get(0),
						OreDictionary.getOres("dust" + ore.substring(5)).get(0), 0.1F);

		}
	}
}
