package mrriegel.furnus.message;

import io.netty.buffer.ByteBuf;
import mrriegel.furnus.block.AbstractMachine;
import mrriegel.furnus.block.AbstractMachine.Direction;
import mrriegel.furnus.block.AbstractMachine.Mode;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PutMessage implements IMessage, IMessageHandler<PutMessage, IMessage> {
	int x, y, z;
	String kind, mode, i;

	public PutMessage() {

	}

	public PutMessage(String i, int x, int y, int z, String kind, String mode) {
		super();
		this.i = i;
		this.x = x;
		this.y = y;
		this.z = z;
		this.kind = kind;
		this.mode = mode;
	}

	@Override
	public IMessage onMessage(PutMessage message, MessageContext ctx) {
		AbstractMachine tile = (AbstractMachine) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
		AbstractMachine.getMap(message.kind, tile).put(Direction.valueOf(message.i), Mode.valueOf(message.mode));
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.i = ByteBufUtils.readUTF8String(buf);
		this.kind = ByteBufUtils.readUTF8String(buf);
		this.mode = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		ByteBufUtils.writeUTF8String(buf, this.i);
		ByteBufUtils.writeUTF8String(buf, this.kind);
		ByteBufUtils.writeUTF8String(buf, this.mode);
	}

}
