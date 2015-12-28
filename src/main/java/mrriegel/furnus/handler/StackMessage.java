package mrriegel.furnus.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import io.netty.buffer.ByteBuf;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.block.TileFurnus.Direction;
import mrriegel.furnus.block.TileFurnus.Mode;
import mrriegel.furnus.gui.IOFGui;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class StackMessage implements IMessage,
		IMessageHandler<StackMessage, IMessage> {
	ItemStack stack;

	public StackMessage() {

	}

	public StackMessage(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public IMessage onMessage(StackMessage message, MessageContext ctx) {
		Minecraft.getMinecraft().thePlayer.inventory
				.setItemStack(message.stack);
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
