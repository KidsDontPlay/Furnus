package mrriegel.furnus.gui;

import java.util.ArrayList;
import java.util.List;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.message.CheckMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class FurnusGUI extends GuiContainer {
	private static final ResourceLocation texture = new ResourceLocation(Furnus.MODID + ":"
			+ "textures/gui/furnus.png");
	Button i, o, f, check;
	TileFurnus tile;

	public FurnusGUI(Container p_i1072_1_) {
		super(p_i1072_1_);
		tile = ((FurnusContainer) inventorySlots).getTile();
		this.ySize = 213;
		this.xSize = 176;
	}

	@Override
	public void initGui() {
		super.initGui();
		check = new Button(0, guiLeft + 46, guiTop + 4, tile.isSplit() ? "X" : " ");
		buttonList.add(check);
		i = new Button(1, guiLeft + 115, guiTop + 108, "I");
		buttonList.add(i);
		o = new Button(2, guiLeft + 134, guiTop + 108, "O");
		buttonList.add(o);
		f = new Button(3, guiLeft + 153, guiTop + 108, "F");
		buttonList.add(f);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_,
			int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		drawMore(k, l);
		if (tile.getSlots() > 0)
			drawString(mc.fontRenderer, StatCollector.translateToLocal("gui.furnus.split"),
					guiLeft + 22, guiTop + 7, 14737632);

	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		if (tile.getSlots() < 1) {
			check.enabled = false;
			check.visible = false;
		} else {
			check.enabled = true;
			check.visible = true;
		}
		if (tile.isInout()) {
			i.enabled = true;
			i.visible = true;
			o.enabled = true;
			o.visible = true;
			f.enabled = true;
			f.visible = true;
		} else {
			i.enabled = false;
			i.visible = false;
			o.enabled = false;
			o.visible = false;
			f.enabled = false;
			f.visible = false;
		}
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		int i = Mouse.getX() * this.width / this.mc.displayWidth;
		int j = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
		if (i > guiLeft + 3 && i < guiLeft + 19 && j > guiTop + 3 && j < guiTop + 19) {
			List<String> list = new ArrayList<String>();
			double speed = (1.d + tile.getSpeed() * 1.d);
			list.add(StatCollector.translateToLocal("gui.furnus.speed") + ": "
					+ String.format("%.2f", speed) + "x");
			int down = 100;
			down /= (-1d / 13d) * tile.getSpeed() + 1d;
			down /= (-1d / 10d) * tile.getBonus() + 1d;
			down *= (-1d / 16d) * tile.getEffi() + 1d;
			double effi = down / 100d;
			list.add(StatCollector.translateToLocal("gui.furnus.effi") + ": "
					+ String.format("%.2f", effi) + "x");
			int bonus = (tile.getBonus() * 10);
			list.add(StatCollector.translateToLocal("gui.furnus.bonus") + ": "
					+ String.format("%d", bonus) + "%");
			double xp = (1.d + tile.getXp() * .25);
			list.add(StatCollector.translateToLocal("gui.furnus.xp") + ": "
					+ String.format("%.2f", xp) + "x");
			GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			this.drawHoveringText(list, i, j, fontRendererObj);
			GL11.glPopAttrib();
			GL11.glPopAttrib();
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id != 0)
			mc.thePlayer.openGui(Furnus.instance, p_146284_1_.id, tile.getWorldObj(), tile.xCoord,
					tile.yCoord, tile.zCoord);
		else {
			if (check.displayString.equals("X"))
				check.displayString = " ";
			else
				check.displayString = "X";
			boolean chek = check.displayString.equals("X");
			tile.setSplit(chek);
			PacketHandler.INSTANCE.sendToServer(new CheckMessage(chek));
		}
	}

	private void drawMore(int k, int l) {
		switch (tile.getSlots()) {
		case 0:
			drawTexturedModalRect(k + 19, l + 47, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47, 176, 31, 18, 18);
			drawTexturedModalRect(k + 42, l + 49, 176, 14,
					(int) (tile.getProgress().get(0) / (200. / 24.)), 17);
			break;
		case 1:
			drawTexturedModalRect(k + 19, l + 47 - 13, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49 - 13, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43 - 13, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47 - 13, 176, 31, 18, 18);
			drawTexturedModalRect(k + 19, l + 47 + 14, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49 + 14, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43 + 14, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47 + 14, 176, 31, 18, 18);
			drawTexturedModalRect(k + 42, l + 49 - 13, 176, 14,
					(int) (tile.getProgress().get(0) / (200. / 24.)), 17);
			drawTexturedModalRect(k + 42, l + 49 + 14, 176, 14,
					(int) (tile.getProgress().get(1) / (200. / 24.)), 17);
			break;
		case 2:
			drawTexturedModalRect(k + 19, l + 47 - 27, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49 - 27, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43 - 27, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47 - 27, 176, 31, 18, 18);
			drawTexturedModalRect(k + 19, l + 47, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47, 176, 31, 18, 18);
			drawTexturedModalRect(k + 19, l + 47 + 27, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49 + 27, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43 + 27, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47 + 27, 176, 31, 18, 18);
			drawTexturedModalRect(k + 42, l + 49 - 27, 176, 14,
					(int) (tile.getProgress().get(0) / (200. / 24.)), 17);
			drawTexturedModalRect(k + 42, l + 49, 176, 14,
					(int) (tile.getProgress().get(1) / (200. / 24.)), 17);
			drawTexturedModalRect(k + 42, l + 49 + 27, 176, 14,
					(int) (tile.getProgress().get(2) / (200. / 24.)), 17);
			break;
		}
		int percent = (int) (((float) tile.getFuel()) / ((float) tile.getMaxFuel()) * 100f);
		int d = 13 - ((int) (14 * (percent / 100.0f)));
		drawTexturedModalRect(k + 45, l + 102 + d, 176, 0 + d, 14, 14 - d);
	}

	class Button extends GuiButton {

		public Button(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, String p_i1021_6_) {
			super(p_i1021_1_, p_i1021_2_, p_i1021_3_, 16, 16, p_i1021_6_);
		}

		@Override
		public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
			if (this.visible) {
				FontRenderer fontrenderer = p_146112_1_.fontRenderer;
				p_146112_1_.getTextureManager().bindTexture(texture);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.field_146123_n = p_146112_2_ >= this.xPosition
						&& p_146112_3_ >= this.yPosition
						&& p_146112_2_ < this.xPosition + this.width
						&& p_146112_3_ < this.yPosition + this.height;
				int k = this.getHoverState(this.field_146123_n);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 160 + 16 * k, 90, 16, 16);
				this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
				int l = 14737632;

				if (packedFGColour != 0) {
					l = packedFGColour;
				} else if (!this.enabled) {
					l = 10526880;
				} else if (this.field_146123_n) {
					l = 16777120;
				}

				this.drawCenteredString(fontrenderer, this.displayString, this.xPosition
						+ this.width / 2, this.yPosition + (this.height - 8) / 2, l);
			}
		}

	}

}