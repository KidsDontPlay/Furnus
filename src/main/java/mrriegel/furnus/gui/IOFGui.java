package mrriegel.furnus.gui;

import java.io.IOException;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.AbstractMachine;
import mrriegel.furnus.block.AbstractMachine.Direction;
import mrriegel.furnus.block.AbstractMachine.Mode;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.block.TilePulvus;
import mrriegel.furnus.handler.GuiHandler;
import mrriegel.limelib.gui.CommonGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

public class IOFGui extends CommonGuiScreen {
	private static final ResourceLocation GuiTextures = new ResourceLocation(Furnus.MODID + ":textures/gui/iof.png");
	AbstractMachine tile;
	Button top, front, left, right, bottom, back;
	String id;

	public IOFGui(AbstractMachine tileEntity, int iD) {
		tile = tileEntity;
		if (iD == 1)
			id = "I";
		else if (iD == 2)
			id = "O";
		else if (iD == 3)
			id = "F";
		xSize = 101;
		ySize = 101;
	}

	@Override
	public void initGui() {
		super.initGui();
		top = new Button(0, guiLeft + 40, guiTop + 20, 20, 20, getMode(Direction.TOP).toString().substring(0, 1).toUpperCase());
		buttonList.add(top);
		front = new Button(1, guiLeft + 40, guiTop + 42, 20, 20, getMode(Direction.FRONT).toString().substring(0, 1).toUpperCase());
		buttonList.add(front);
		left = new Button(2, guiLeft + 18, guiTop + 42, 20, 20, getMode(Direction.LEFT).toString().substring(0, 1).toUpperCase());
		buttonList.add(left);
		right = new Button(3, guiLeft + 62, guiTop + 42, 20, 20, getMode(Direction.RIGHT).toString().substring(0, 1).toUpperCase());
		buttonList.add(right);
		bottom = new Button(4, guiLeft + 40, guiTop + 64, 20, 20, getMode(Direction.BOTTOM).toString().substring(0, 1).toUpperCase());
		buttonList.add(bottom);
		back = new Button(5, guiLeft + 62, guiTop + 64, 20, 20, getMode(Direction.BACK).toString().substring(0, 1).toUpperCase());
		buttonList.add(back);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("id", 1);
		nbt.setInteger("dir", button.id);
		nbt.setString("kind", id);
		tile.sendMessage(nbt);
		tile.handleMessage(mc.thePlayer, nbt);

		top.displayString = getMode(Direction.TOP).toString().substring(0, 1).toUpperCase();
		front.displayString = getMode(Direction.FRONT).toString().substring(0, 1).toUpperCase();
		left.displayString = getMode(Direction.LEFT).toString().substring(0, 1).toUpperCase();
		right.displayString = getMode(Direction.RIGHT).toString().substring(0, 1).toUpperCase();
		bottom.displayString = getMode(Direction.BOTTOM).toString().substring(0, 1).toUpperCase();
		back.displayString = getMode(Direction.BACK).toString().substring(0, 1).toUpperCase();
	}

	private Mode getMode(Direction d) {
		return tile.getMap(id).get(d);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiTextures);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		super.drawScreen(mouseX, mouseY, partialTicks);
		String pre = "gui.furnus.";
		mc.fontRendererObj.drawString(id.equals("F") ? I18n.format(pre + "fuel") : id.equals("I") ? I18n.format(pre + "input") : I18n.format(pre + "output"), guiLeft + 8, guiTop + 6, 4210752);
		if (top.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "top") + " - " + I18n.format(pre + getMode(Direction.TOP).toString())), mouseX, mouseY);
		if (bottom.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "bottom") + " - " + I18n.format(pre + getMode(Direction.BOTTOM).toString())), mouseX, mouseY);
		if (right.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "right") + " - " + I18n.format(pre + getMode(Direction.RIGHT).toString())), mouseX, mouseY);
		if (left.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "left") + " - " + I18n.format(pre + getMode(Direction.LEFT).toString())), mouseX, mouseY);
		if (back.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "back") + " - " + I18n.format(pre + getMode(Direction.BACK).toString())), mouseX, mouseY);
		if (front.isMouseOver())
			drawHoveringText(Lists.newArrayList(I18n.format(pre + "front") + " - " + I18n.format(pre + getMode(Direction.FRONT).toString())), mouseX, mouseY);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			int id = tile instanceof TileFurnus ? GuiHandler.FURNUS : tile instanceof TilePulvus ? GuiHandler.PULVUS : -1;
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("gui", id);
			nbt.setInteger("id", 2);
			tile.sendMessage(nbt);
		} else
			super.keyTyped(typedChar, keyCode);
	}

	class Button extends GuiButton {

		public Button(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_, String p_i1021_6_) {
			super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, p_i1021_6_);
		}

		@Override
		public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
			if (this.visible) {
				p_146112_1_.getTextureManager().bindTexture(BUTTON_TEXTURES);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.hovered = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
				int k = this.getHoverState(this.hovered);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
				this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
				p_146112_1_.getTextureManager().bindTexture(GuiTextures);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 101, 0 + (displayString.equals(Mode.ENABLED.toString().substring(0, 1).toUpperCase()) ? 0 : displayString.equals(Mode.X.toString().substring(0, 1).toUpperCase()) ? 20 : 40), 20, 20);

			}
		}

	}
}
