package mrriegel.furnus.message;

import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import mrriegel.furnus.block.TileFurnus;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ProgressMessage implements IMessage, IMessageHandler<ProgressMessage, IMessage> {
	boolean burning;
	int x, y, z, fuel;
	Map<Integer, Integer> progress;

	public ProgressMessage() {

	}

	public ProgressMessage(boolean burning, int x, int y, int z, int fuel,
			Map<Integer, Integer> progress) {
		this.burning = burning;
		this.x = x;
		this.y = y;
		this.z = z;
		this.fuel = fuel;
		this.progress = progress;
	}

	@Override
	public IMessage onMessage(ProgressMessage message, MessageContext ctx) {
		TileFurnus tile = (TileFurnus) Minecraft.getMinecraft().theWorld.getTileEntity(message.x,
				message.y, message.z);
		if (tile == null)
			return null;
		tile.setProgress(message.progress);
		tile.setFuel(message.fuel);
		tile.setBurning(message.burning);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.burning = buf.readBoolean();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.fuel = buf.readInt();
		this.progress = new Gson().fromJson(ByteBufUtils.readUTF8String(buf),
				new TypeToken<Map<Integer, Integer>>() {
				}.getType());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.burning);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.fuel);
		ByteBufUtils.writeUTF8String(buf, new Gson().toJson(this.progress));

	}

}
