package mrriegel.furnus.message;

import io.netty.buffer.ByteBuf;
import mrriegel.furnus.block.AbstractMachine;
import mrriegel.furnus.block.AbstractMachine.Direction;
import mrriegel.furnus.block.AbstractMachine.Mode;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
	public IMessage onMessage(final PutMessage message, final MessageContext ctx) {
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
		mainThread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if (ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z)) instanceof AbstractMachine) {
					AbstractMachine tile = (AbstractMachine) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
					AbstractMachine.getMap(message.kind, tile).put(Direction.valueOf(message.i), Mode.valueOf(message.mode));
				}
			}
		});
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
