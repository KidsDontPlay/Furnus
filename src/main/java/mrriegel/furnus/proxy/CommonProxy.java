package mrriegel.furnus.proxy;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.init.ModBlocks;
import mrriegel.furnus.init.ModConfig;
import mrriegel.furnus.init.ModItems;
import mrriegel.furnus.init.ModRecipes;
import mrriegel.furnus.util.CrushRecipe;
import mrriegel.furnus.util.GuiHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ModConfig.refreshConfig(event.getSuggestedConfigurationFile());
		ModItems.init();
		ModBlocks.init();
		ModRecipes.init();
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Furnus.instance, new GuiHandler());
		OreDictionary.registerOre("dustIron", new ItemStack(ModItems.dust, 1, 0));
		OreDictionary.registerOre("dustGold", new ItemStack(ModItems.dust, 1, 1));
	}

	public void postInit(FMLPostInitializationEvent event) {
		for (CrushRecipe r : ModConfig.recipes)
			r.register();
		CrushRecipe.registerDefaultOreRecipes();
	}

}
