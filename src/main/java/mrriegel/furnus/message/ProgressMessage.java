package mrriegel.furnus.message;

import io.netty.buffer.ByteBuf;

import java.util.Map;

import mrriegel.furnus.block.AbstractMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class ProgressMessage implements IMessage, IMessageHandler<ProgressMessage, IMessage> {
	boolean burning;
	int x, y, z, fuel, maxFuel, rf, ticks;
	Map<Integer, Integer> progress;

	public ProgressMessage() {
	}

	public ProgressMessage(boolean burning, int x, int y, int z, int fuel, int maxFuel, Map<Integer, Integer> progress, int rf, int ticks) {
		this.burning = burning;
		this.x = x;
		this.y = y;
		this.z = z;
		this.fuel = fuel;
		this.maxFuel = maxFuel;
		this.progress = progress;
		this.rf = rf;
		this.ticks = ticks;
	}

	@Override
	public IMessage onMessage(final ProgressMessage message, MessageContext ctx) {
		IThreadListener mainThread = Minecraft.getMinecraft();
		mainThread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if (Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z)) instanceof AbstractMachine) {
					try {
						AbstractMachine tile = (AbstractMachine) Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
						if (tile == null)
							return;
						tile.setProgress(message.progress);
						tile.setFuel(message.fuel);
						tile.setMaxFuel(message.maxFuel);
						tile.setBurning(message.burning);
						tile.setRemainTicks(message.ticks);
						tile.en.setEnergyStored(message.rf);
						return;
					} catch (NullPointerException e) {
						return;
					}
				}
			}
		});
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.burning = buf.readBoolean();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.fuel = buf.readInt();
		this.maxFuel = buf.readInt();
		this.rf = buf.readInt();
		this.ticks = buf.readInt();
		this.progress = new Gson().fromJson(ByteBufUtils.readUTF8String(buf), new TypeToken<Map<Integer, Integer>>() {
		}.getType());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.burning);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.fuel);
		buf.writeInt(this.maxFuel);
		buf.writeInt(this.rf);
		buf.writeInt(this.ticks);
		ByteBufUtils.writeUTF8String(buf, new Gson().toJson(this.progress));

	}

}
