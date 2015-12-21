package mrriegel.furnus.gui;

import java.util.Map;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.handler.OpenMessage;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.handler.PutMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class IOFGui extends GuiScreen {
	private static final ResourceLocation GuiTextures = new ResourceLocation(
			Furnus.MODID + ":textures/gui/iof.png");
	TileFurnus tile;
	GuiButton top, front, left, right, bottom, back;
	int imageWidth = 101;
	int imageHeight = 101;
	int guiLeft, guiTop;
	String id;

	public enum Mode {
		NORMAL, AUTO, X;
		private static Mode[] vals = values();

		public Mode next() {
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}

	public enum Direction {
		TOP, RIGHT, LEFT, FRONT, BOTTOM, BACK;
	}

	Mode topMode, frontMode, leftMode, rightMode, bottomMode, backMode;

	public IOFGui(TileFurnus tileEntity, int iD) {
		tile = tileEntity;
		if (iD == 1)
			id = "I";
		else if (iD == 2)
			id = "O";
		else if (iD == 3)
			id = "F";

		topMode = getMap(id, tile).get(0);
		frontMode = getMap(id, tile).get(1);
		leftMode = getMap(id, tile).get(2);
		rightMode = getMap(id, tile).get(3);
		bottomMode = getMap(id, tile).get(4);
		backMode = getMap(id, tile).get(5);
	}

	@Override
	public void initGui() {
		super.initGui();
		guiLeft = (this.width - this.imageWidth) / 2;
		guiTop = (this.height - this.imageHeight) / 2;
		top = new GuiButton(0, guiLeft + 40, guiTop + 20, 20, 20, topMode
				.toString().substring(0, 1).toUpperCase());
		buttonList.add(top);
		front = new GuiButton(1, guiLeft + 40, guiTop + 42, 20, 20, frontMode
				.toString().substring(0, 1).toUpperCase());
		buttonList.add(front);
		left = new GuiButton(2, guiLeft + 18, guiTop + 42, 20, 20, leftMode
				.toString().substring(0, 1).toUpperCase());
		buttonList.add(left);
		right = new GuiButton(3, guiLeft + 62, guiTop + 42, 20, 20, rightMode
				.toString().substring(0, 1).toUpperCase());
		buttonList.add(right);
		bottom = new GuiButton(4, guiLeft + 40, guiTop + 64, 20, 20, bottomMode
				.toString().substring(0, 1).toUpperCase());
		buttonList.add(bottom);
		back = new GuiButton(5, guiLeft + 62, guiTop + 64, 20, 20, backMode
				.toString().substring(0, 1).toUpperCase());
		buttonList.add(back);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch (p_146284_1_.id) {
		case 0:
			topMode = topMode.next();
			top.displayString = topMode.toString().substring(0, 1)
					.toUpperCase();
			getMap(id, tile).put(Direction.TOP, topMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.TOP
					.toString(), tile.xCoord, tile.yCoord, tile.zCoord, id,
					topMode.toString()));
			break;
		case 1:
			frontMode = frontMode.next();
			front.displayString = frontMode.toString().substring(0, 1)
					.toUpperCase();
			getMap(id, tile).put(Direction.FRONT, frontMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.FRONT
					.toString(), tile.xCoord, tile.yCoord, tile.zCoord, id,
					frontMode.toString()));
			break;
		case 2:
			leftMode = leftMode.next();
			left.displayString = leftMode.toString().substring(0, 1)
					.toUpperCase();
			getMap(id, tile).put(Direction.LEFT, leftMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.LEFT
					.toString(), tile.xCoord, tile.yCoord, tile.zCoord, id,
					leftMode.toString()));
			break;
		case 3:
			rightMode = rightMode.next();
			right.displayString = rightMode.toString().substring(0, 1)
					.toUpperCase();
			getMap(id, tile).put(Direction.RIGHT, rightMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.RIGHT
					.toString(), tile.xCoord, tile.yCoord, tile.zCoord, id,
					rightMode.toString()));
			break;
		case 4:
			bottomMode = bottomMode.next();
			bottom.displayString = bottomMode.toString().substring(0, 1)
					.toUpperCase();
			getMap(id, tile).put(Direction.BOTTOM, bottomMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.BOTTOM
					.toString(), tile.xCoord, tile.yCoord, tile.zCoord, id,
					bottomMode.toString()));
			break;
		case 5:
			backMode = backMode.next();
			back.displayString = backMode.toString().substring(0, 1)
					.toUpperCase();
			getMap(id, tile).put(Direction.BACK, backMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.BACK
					.toString(), tile.xCoord, tile.yCoord, tile.zCoord, id,
					backMode.toString()));
			break;
		}
	}

	public static Map<Direction, IOFGui.Mode> getMap(String id, TileFurnus tile) {
		if (id.equals("I"))
			return tile.getInput();
		else if (id.equals("O"))
			return tile.getOutput();
		else if (id.equals("F"))
			return tile.getFuelput();
		return null;
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiTextures);
		guiLeft = (this.width - this.imageWidth) / 2;
		guiTop = (this.height - this.imageHeight) / 2;
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.imageWidth,
				this.imageHeight);

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		// GL11.glDisable(GL11.GL_BLEND);
		// drawTexturedModalRect(top.xPosition, top.yPosition, 100, 0, 20, 20);
		mc.fontRenderer.drawString(id.equals("F") ? "Fuel"
				: id.equals("I") ? "Input" : "Output", guiLeft + 8, guiTop + 6,
				4210752);

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		PacketHandler.INSTANCE.sendToServer(new OpenMessage(tile.xCoord,
				tile.yCoord, tile.zCoord));
	}
}
