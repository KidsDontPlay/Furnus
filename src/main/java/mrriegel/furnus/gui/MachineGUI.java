package mrriegel.furnus.gui;

import java.util.ArrayList;
import java.util.List;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.AbstractMachine;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.block.TilePulvus;
import mrriegel.furnus.handler.ConfigHandler;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.message.CheckMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

public class MachineGUI extends GuiContainer {
	private static ResourceLocation texture;
	Button i, o, f, check;
	AbstractMachine tile;

	public MachineGUI(Container p_i1072_1_) {
		super(p_i1072_1_);
		tile = ((MachineContainer) inventorySlots).getTile();
		if (tile instanceof TileFurnus)
			texture = new ResourceLocation(Furnus.MODID + ":" + "textures/gui/furnus.png");
		else if (tile instanceof TilePulvus)
			texture = new ResourceLocation(Furnus.MODID + ":" + "textures/gui/pulvus.png");
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
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		drawMore(k, l);
		if (tile.getSlots() > 0)
			mc.fontRendererObj.drawString(I18n.format("gui.furnus.split"), guiLeft + 22, guiTop + 7, 4210752);

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
			double speed = (1.d + tile.getSpeed() * 1.d * ConfigHandler.speedMulti);
			list.add(I18n.format("gui.furnus.speed") + ": " + String.format("%.2f", speed) + "x");
			double effi = (tile.getSpeed() * (ConfigHandler.speedFuelMulti / 10.) + tile.getBonus() * (ConfigHandler.bonusFuelMulti / 10.) + 1.) / (tile.getEffi() * (ConfigHandler.effiMulti / 10.) + 1.);
			list.add(I18n.format("gui.furnus.effi") + ": " + String.format("%.2f", effi) + "x");
			int bonus = (tile.getBonus() * (int) (ConfigHandler.bonusMulti * 100.));
			list.add(I18n.format("gui.furnus.bonus") + ": " + String.format("%d", bonus) + "%");
			double xp = (1.d + tile.getXp() * ConfigHandler.xpMulti);
			list.add(I18n.format("gui.furnus.xp") + ": " + String.format("%.2f", xp) + "x");
			if (tile.isRf())
				list.add("RF: " + tile.en.getEnergyStored() + "/" + tile.en.getMaxEnergyStored());
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			this.drawHoveringText(list, i, j, fontRendererObj);
			GlStateManager.popMatrix();
			GlStateManager.enableLighting();
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (i != null && i.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format("gui.furnus.input")), mouseX - guiLeft, mouseY - guiTop);
		if (o != null && o.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format("gui.furnus.output")), mouseX - guiLeft, mouseY - guiTop);
		if (f != null && f.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format("gui.furnus.fuel")), mouseX - guiLeft, mouseY - guiTop);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			if (check.displayString.equals("X"))
				check.displayString = " ";
			else
				check.displayString = "X";
			boolean chek = check.displayString.equals("X");
			tile.setSplit(chek);
			PacketHandler.INSTANCE.sendToServer(new CheckMessage(chek));
		} else {
			mc.thePlayer.closeScreen();
			mc.thePlayer.openGui(Furnus.instance, p_146284_1_.id, tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
		}
	}

	private void drawMore(int k, int l) {
		switch (tile.getSlots()) {
		case 0:
			drawTexturedModalRect(k + 19, l + 47, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47, 176, 31, 18, 18);
			drawTexturedModalRect(k + 42, l + 49, 176, 14, (int) (tile.getProgress().get(0) / (200. / 24.)), 17);
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
			drawTexturedModalRect(k + 42, l + 49 - 13, 176, 14, (int) (tile.getProgress().get(0) / (200. / 24.)), 17);
			drawTexturedModalRect(k + 42, l + 49 + 14, 176, 14, (int) (tile.getProgress().get(1) / (200. / 24.)), 17);
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
			drawTexturedModalRect(k + 42, l + 49 - 27, 176, 14, (int) (tile.getProgress().get(0) / (200. / 24.)), 17);
			drawTexturedModalRect(k + 42, l + 49, 176, 14, (int) (tile.getProgress().get(1) / (200. / 24.)), 17);
			drawTexturedModalRect(k + 42, l + 49 + 27, 176, 14, (int) (tile.getProgress().get(2) / (200. / 24.)), 17);
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
				FontRenderer fontrenderer = p_146112_1_.fontRendererObj;
				p_146112_1_.getTextureManager().bindTexture(texture);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.hovered = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
				int k = this.getHoverState(this.hovered);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 160 + 16 * k, 90, 16, 16);
				this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
				int l = 14737632;

				if (packedFGColour != 0) {
					l = packedFGColour;
				} else if (!this.enabled) {
					l = 10526880;
				} else if (this.hovered) {
					l = 16777120;
				}

				this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
			}
		}

	}

}