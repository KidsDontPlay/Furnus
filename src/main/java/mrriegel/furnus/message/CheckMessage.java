package mrriegel.furnus.message;

import io.netty.buffer.ByteBuf;
import mrriegel.furnus.gui.MachineContainer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CheckMessage implements IMessage, IMessageHandler<CheckMessage, IMessage> {
	boolean split;

	public CheckMessage() {
	}

	public CheckMessage(boolean split) {
		this.split = split;
	}

	@Override
	public IMessage onMessage(final CheckMessage message, final MessageContext ctx) {
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
		mainThread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				((MachineContainer) ctx.getServerHandler().playerEntity.openContainer).getTile().setSplit(message.split);
			}
		});
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.split = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.split);
	}

}
