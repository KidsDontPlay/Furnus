package mrriegel.furnus;

import mrriegel.furnus.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	@EventHandler
	public void s(FMLLoadCompleteEvent event){
	}

}
