package mrriegel.furnus;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import mrriegel.furnus.block.ModBlocks;
import mrriegel.furnus.handler.ConfigurationHandler;
import mrriegel.furnus.handler.CrunchHandler;
import mrriegel.furnus.handler.GuiHandler;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.item.ItemDust.Dust;
import mrriegel.furnus.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

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
		if (event.getSide() == Side.CLIENT) {
			initModels();
		}
	}

	private void initModels() {
		Minecraft
				.getMinecraft()
				.getRenderItem()
				.getItemModelMesher()
				.register(Item.getItemFromBlock(ModBlocks.furnus), 0,
						new ModelResourceLocation(Furnus.MODID + ":" + "furnus", "inventory"));
		Minecraft
				.getMinecraft()
				.getRenderItem()
				.getItemModelMesher()
				.register(Item.getItemFromBlock(ModBlocks.pulvus), 0,
						new ModelResourceLocation(Furnus.MODID + ":" + "pulvus", "inventory"));
		for (int i = 0; i < 7; i++) {
			ModelBakery.registerItemVariants(ModItems.upgrade, new ResourceLocation(Furnus.MODID
					+ ":" + "upgrade_" + i));
			Minecraft
					.getMinecraft()
					.getRenderItem()
					.getItemModelMesher()
					.register(
							ModItems.upgrade,
							i,
							new ModelResourceLocation(Furnus.MODID + ":" + "upgrade_" + i,
									"inventory"));
		}
		for (int i = 0; i < Dust.values().length; i++) {
			ModelBakery.registerItemVariants(ModItems.dust, new ResourceLocation(Furnus.MODID + ":"
					+ "dust_" + Dust.values()[i].toString().toLowerCase()));
			Minecraft
					.getMinecraft()
					.getRenderItem()
					.getItemModelMesher()
					.register(
							ModItems.dust,
							i,
							new ModelResourceLocation(Furnus.MODID + ":" + "dust_"
									+ Dust.values()[i].toString().toLowerCase(), "inventory"));
		}
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
