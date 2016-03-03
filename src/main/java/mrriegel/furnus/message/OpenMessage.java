package mrriegel.furnus.message;

import io.netty.buffer.ByteBuf;
import mrriegel.furnus.Furnus;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class OpenMessage implements IMessage, IMessageHandler<OpenMessage, IMessage> {
	int x, y, z, id;

	public OpenMessage() {
	}

	public OpenMessage(int x, int y, int z, int id) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
	}

	@Override
	public IMessage onMessage(OpenMessage message, MessageContext ctx) {
		ctx.getServerHandler().playerEntity.openGui(Furnus.instance, message.id, ctx.getServerHandler().playerEntity.worldObj, message.x, message.y, message.z);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.id);
	}

}
