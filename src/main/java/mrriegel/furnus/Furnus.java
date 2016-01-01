package mrriegel.furnus;

import mrriegel.furnus.block.BlockFurnus;
import mrriegel.furnus.handler.GuiHandler;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.item.ItemUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Furnus.MODID, name = Furnus.MODNAME, version = Furnus.VERSION)
public class Furnus {
	public static final String MODID = "furnus";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "Furnus";

	@Instance(Furnus.MODID)
	public static Furnus instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// File configFile = event.getSuggestedConfigurationFile();
		// ConfigurationHandler.config = new Configuration(configFile);
		// ConfigurationHandler.config.load();
		// ConfigurationHandler.refreshConfig();
		PacketHandler.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		BlockFurnus.init();
		ItemUpgrade.init();
		CraftingRecipes.init();
		if (event.getSide() == Side.CLIENT) {
			initModels();
		}
	}

	private void initModels() {
		Minecraft
				.getMinecraft()
				.getRenderItem()
				.getItemModelMesher()
				.register(ItemUpgrade.upgrade, 0,
						new ModelResourceLocation(Furnus.MODID + ":" + "upgrade", "inventory"));
	}
}
