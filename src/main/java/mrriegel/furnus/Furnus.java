package mrriegel.furnus;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import mrriegel.furnus.proxy.CommonProxy;
import mrriegel.limelib.LimeCommonProxy;
import mrriegel.limelib.LimeLib;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = Furnus.MODID, name = Furnus.MODNAME, version = Furnus.VERSION, dependencies = "required-after:limelib@[1.6.0,)")
public class Furnus {
	public static final String MODID = "furnus";
	public static final String VERSION = "2.0.0";
	public static final String MODNAME = "Furnus";

	@Instance(Furnus.MODID)
	public static Furnus instance;

	@SidedProxy(clientSide = "mrriegel.furnus.proxy.CommonProxy", serverSide = "mrriegel.furnus.proxy.ClientProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
