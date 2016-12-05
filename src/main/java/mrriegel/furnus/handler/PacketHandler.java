package mrriegel.furnus.handler;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.message.CheckMessage;
import mrriegel.furnus.message.OpenMessage;
import mrriegel.furnus.message.ProgressMessage;
import mrriegel.furnus.message.PutMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Furnus.MODID);

	public static void init() {
		int id = 0;
		INSTANCE.registerMessage(CheckMessage.class, CheckMessage.class, id++, Side.SERVER);
		INSTANCE.registerMessage(OpenMessage.class, OpenMessage.class, id++, Side.SERVER);
		INSTANCE.registerMessage(PutMessage.class, PutMessage.class, id++, Side.SERVER);
		INSTANCE.registerMessage(ProgressMessage.class, ProgressMessage.class, id++, Side.CLIENT);
	}
}