package mrriegel.furnus;

import mrriegel.furnus.proxy.CommonProxy;
import mrriegel.limelib.LimeLib;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Furnus.MODID, name = Furnus.MODNAME, version = Furnus.VERSION, dependencies = "required-after:limelib@[1.6.0,)")
public class Furnus {
	public static final String MODID = "furnus";
	public static final String VERSION = "2.0.0";
	public static final String MODNAME = "Furnus";

	@Instance(Furnus.MODID)
	public static Furnus instance;

	@SidedProxy(serverSide = "mrriegel.furnus.proxy.CommonProxy", clientSide = "mrriegel.furnus.proxy.ClientProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		MinecraftForge.EVENT_BUS.register(this);	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	@SubscribeEvent
	public void ee(TextureStitchEvent.Pre event){
		LimeLib.log.info("zap");
	}

}
