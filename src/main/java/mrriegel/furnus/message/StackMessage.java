package mrriegel.furnus.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StackMessage implements IMessage, IMessageHandler<StackMessage, IMessage> {
	ItemStack stack;

	public StackMessage() {

	}

	public StackMessage(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public IMessage onMessage(StackMessage message, MessageContext ctx) {
		Minecraft.getMinecraft().thePlayer.inventory.setItemStack(message.stack);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.stack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeItemStack(buf, this.stack);
	}

}
