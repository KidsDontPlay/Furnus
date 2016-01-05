package mrriegel.furnus.message;

import io.netty.buffer.ByteBuf;
import mrriegel.furnus.gui.MachineContainer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class CheckMessage implements IMessage, IMessageHandler<CheckMessage, IMessage> {
	boolean split;

	public CheckMessage() {
	}

	public CheckMessage(boolean split) {
		this.split = split;
	}

	@Override
	public IMessage onMessage(CheckMessage message, MessageContext ctx) {
		((MachineContainer) ctx.getServerHandler().playerEntity.openContainer).getTile().setSplit(
				message.split);
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
