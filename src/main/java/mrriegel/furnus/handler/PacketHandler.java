package mrriegel.furnus.handler;

import mrriegel.furnus.Furnus;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(
			Furnus.MODID);

	public static void init() {
		int id = 0;
		INSTANCE.registerMessage(CheckMessage.class, CheckMessage.class, id++,
				Side.SERVER);
		INSTANCE.registerMessage(OpenMessage.class, OpenMessage.class, id++,
				Side.SERVER);
		INSTANCE.registerMessage(PutMessage.class, PutMessage.class, id++,
				Side.SERVER);
		INSTANCE.registerMessage(StackMessage.class, StackMessage.class, id++,
				Side.CLIENT);
	}
}