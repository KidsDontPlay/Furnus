package mrriegel.furnus.gui;

import java.util.Map;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.block.TileFurnus.Direction;
import mrriegel.furnus.block.TileFurnus.Mode;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.message.OpenMessage;
import mrriegel.furnus.message.PutMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class IOFGui extends GuiScreen {
	private static final ResourceLocation GuiTextures = new ResourceLocation(Furnus.MODID
			+ ":textures/gui/iof.png");
	TileFurnus tile;
	Button top, front, left, right, bottom, back;
	int imageWidth = 101;
	int imageHeight = 101;
	int guiLeft, guiTop;
	String id;

	Mode topMode, frontMode, leftMode, rightMode, bottomMode, backMode;

	public IOFGui(TileFurnus tileEntity, int iD) {
		tile = tileEntity;
		if (iD == 1)
			id = "I";
		else if (iD == 2)
			id = "O";
		else if (iD == 3)
			id = "F";

		topMode = getMap(id, tile).get(Direction.TOP);
		frontMode = getMap(id, tile).get(Direction.FRONT);
		leftMode = getMap(id, tile).get(Direction.LEFT);
		rightMode = getMap(id, tile).get(Direction.RIGHT);
		bottomMode = getMap(id, tile).get(Direction.BOTTOM);
		backMode = getMap(id, tile).get(Direction.BACK);
	}

	@Override
	public void initGui() {
		super.initGui();
		guiLeft = (this.width - this.imageWidth) / 2;
		guiTop = (this.height - this.imageHeight) / 2;
		top = new Button(0, guiLeft + 40, guiTop + 20, 20, 20, topMode.toString().substring(0, 1)
				.toUpperCase());
		buttonList.add(top);
		front = new Button(1, guiLeft + 40, guiTop + 42, 20, 20, frontMode.toString()
				.substring(0, 1).toUpperCase());
		buttonList.add(front);
		left = new Button(2, guiLeft + 18, guiTop + 42, 20, 20, leftMode.toString().substring(0, 1)
				.toUpperCase());
		buttonList.add(left);
		right = new Button(3, guiLeft + 62, guiTop + 42, 20, 20, rightMode.toString()
				.substring(0, 1).toUpperCase());
		buttonList.add(right);
		bottom = new Button(4, guiLeft + 40, guiTop + 64, 20, 20, bottomMode.toString()
				.substring(0, 1).toUpperCase());
		buttonList.add(bottom);
		back = new Button(5, guiLeft + 62, guiTop + 64, 20, 20, backMode.toString().substring(0, 1)
				.toUpperCase());
		buttonList.add(back);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch (p_146284_1_.id) {
		case 0:
			topMode = topMode.next();
			top.displayString = topMode.toString().substring(0, 1).toUpperCase();
			getMap(id, tile).put(Direction.TOP, topMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.TOP.toString(),
					tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, topMode.toString()));
			break;
		case 1:
			frontMode = frontMode.next();
			front.displayString = frontMode.toString().substring(0, 1).toUpperCase();
			getMap(id, tile).put(Direction.FRONT, frontMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.FRONT.toString(),
					tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, frontMode.toString()));
			break;
		case 2:
			leftMode = leftMode.next();
			left.displayString = leftMode.toString().substring(0, 1).toUpperCase();
			getMap(id, tile).put(Direction.LEFT, leftMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.LEFT.toString(),
					tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, leftMode.toString()));
			break;
		case 3:
			rightMode = rightMode.next();
			right.displayString = rightMode.toString().substring(0, 1).toUpperCase();
			getMap(id, tile).put(Direction.RIGHT, rightMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.RIGHT.toString(),
					tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, rightMode.toString()));
			break;
		case 4:
			bottomMode = bottomMode.next();
			bottom.displayString = bottomMode.toString().substring(0, 1).toUpperCase();
			getMap(id, tile).put(Direction.BOTTOM, bottomMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.BOTTOM.toString(),
					tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, bottomMode.toString()));
			break;
		case 5:
			backMode = backMode.next();
			back.displayString = backMode.toString().substring(0, 1).toUpperCase();
			getMap(id, tile).put(Direction.BACK, backMode);
			PacketHandler.INSTANCE.sendToServer(new PutMessage(Direction.BACK.toString(),
					tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), id, backMode.toString()));
			break;
		}
	}

	public static Map<Direction, Mode> getMap(String id, TileFurnus tile) {
		if (id.equals("I"))
			return tile.getInput();
		if (id.equals("O"))
			return tile.getOutput();
		if (id.equals("F"))
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
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.imageWidth, this.imageHeight);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		String pre = "gui.furnus.";
		mc.fontRendererObj.drawString(
				id.equals("F") ? StatCollector.translateToLocal(pre + "fuel")
						: id.equals("I") ? StatCollector.translateToLocal(pre + "input")
								: StatCollector.translateToLocal(pre + "output"), guiLeft + 8,
				guiTop + 6, 4210752);

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		PacketHandler.INSTANCE.sendToServer(new OpenMessage(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ()));
	}

	class Button extends GuiButton {

		public Button(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_,
				int p_i1021_5_, String p_i1021_6_) {
			super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, p_i1021_6_);
		}

		@Override
		public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
			if (this.visible) {
				p_146112_1_.getTextureManager().bindTexture(buttonTextures);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.hovered = p_146112_2_ >= this.xPosition
						&& p_146112_3_ >= this.yPosition
						&& p_146112_2_ < this.xPosition + this.width
						&& p_146112_3_ < this.yPosition + this.height;
				int k = this.getHoverState(this.hovered);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20,
						this.width / 2, this.height);
				this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition,
						200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
				p_146112_1_.getTextureManager().bindTexture(GuiTextures);
				this.drawTexturedModalRect(
						this.xPosition,
						this.yPosition,
						101,
						0 + (displayString.equals(Mode.ENABLED.toString().substring(0, 1)
								.toUpperCase()) ? 0 : displayString.equals(Mode.X.toString()
								.substring(0, 1).toUpperCase()) ? 20 : 40), 20, 20);

			}
		}

	}
}
