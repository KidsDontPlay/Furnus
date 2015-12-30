package mrriegel.furnus.gui;

import java.util.ArrayList;
import java.util.List;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.message.CheckMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class FurnusGUI extends GuiContainer {
	private static final ResourceLocation texture = new ResourceLocation(Furnus.MODID + ":"
			+ "textures/gui/furnus.png");
	GuiButton i, o, f, check;
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
		check = new GuiButton(0, guiLeft + 21, guiTop + 6, 11, 11, tile.isSplit() ? "x" : " ");
		buttonList.add(check);
		i = new GuiButton(1, guiLeft + 130, guiTop + 108, 11, 11, "I");
		buttonList.add(i);
		o = new GuiButton(2, guiLeft + 144, guiTop + 108, 11, 11, "O");
		buttonList.add(o);
		f = new GuiButton(3, guiLeft + 158, guiTop + 108, 11, 11, "F");
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
			drawString(mc.fontRenderer, "Split", guiLeft + 33, guiTop + 7, 14737632);

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
			list.add("Speed: " + String.format("%.2f", speed) + "x");
			int down = 100;
			down /= (-1d / 13d) * tile.getSpeed() + 1d;
			down /= (-1d / 10d) * tile.getBonus() + 1d;
			down *= (-1d / 16d) * tile.getEffi() + 1d;
			double effi = down / 100d;
			list.add("Efficiency: " + String.format("%.2f", effi) + "x");
			int bonus = (tile.getBonus() * 10);
			list.add("Bonus: " + String.format("%d", bonus) + "%");
			double xp = (1.d + tile.getXp() * .25);
			list.add("XP: " + String.format("%.2f", xp) + "x");
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
			if (check.displayString.equals("x"))
				check.displayString = " ";
			else
				check.displayString = "x";
			boolean chek = check.displayString.equals("x");
			tile.setSplit(chek);
			PacketHandler.INSTANCE.sendToServer(new CheckMessage(chek));
		}
	}

	private void drawMore(int k, int l) {
		if (tile.getSlots() > 0) {
			drawTexturedModalRect(k + 19, l + 47, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47, 176, 31, 18, 18);
		}
		if (tile.getSlots() > 1) {
			drawTexturedModalRect(k + 19, l + 47 + 27, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49 + 27, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43 + 27, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47 + 27, 176, 31, 18, 18);
		}
		int percent = (int) (((float) tile.getFuel()) / ((float) tile.getMaxFuel()) * 100f);
		int d = 13 - ((int) (14 * (percent / 100.0f)));
		drawTexturedModalRect(k + 45, l + 102 + d, 176, 0 + d, 14, 14 - d);

		drawTexturedModalRect(k + 42, l + 49 - 27, 176, 14, tile.getProgress().get(0) / 8, 17);
		if (tile.getSlots() > 0)
			drawTexturedModalRect(k + 42, l + 49, 176, 14, tile.getProgress().get(1) / 8, 17);
		if (tile.getSlots() > 1)
			drawTexturedModalRect(k + 42, l + 49 + 27, 176, 14, tile.getProgress().get(2) / 8, 17);

	}

}