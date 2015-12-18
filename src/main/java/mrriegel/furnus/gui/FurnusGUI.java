package mrriegel.furnus.gui;

import java.util.ArrayList;
import java.util.List;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.handler.CheckMessage;
import mrriegel.furnus.handler.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.config.GuiCheckBox;

public class FurnusGUI extends GuiContainer {
	private static final ResourceLocation texture = new ResourceLocation(
			Furnus.MODID + ":" + "textures/gui/furnus.png");
	GuiCheckBox check;
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
		check = new GuiCheckBox(0, guiLeft + 21, guiTop + 6, "Split",
				tile.isSplit()) {
			@Override
			public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_,
					int p_146116_3_) {
				if (this.enabled && this.visible
						&& p_146116_2_ >= this.xPosition
						&& p_146116_3_ >= this.yPosition
						&& p_146116_2_ < this.xPosition + this.width
						&& p_146116_3_ < this.yPosition + this.height) {
					this.setIsChecked(!this.isChecked());
					tile.setSplit(this.isChecked());
					PacketHandler.INSTANCE.sendToServer(new CheckMessage(this
							.isChecked()));
					return true;
				}

				return false;
			}
		};
		check.enabled = true;
		buttonList.add(check);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		drawMore(k, l);

	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		if (tile.getSlot() < 1) {
			check.enabled = false;
			check.visible = false;
		} else {
			check.enabled = true;
			check.visible = true;
		}
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		int i = Mouse.getX() * this.width / this.mc.displayWidth;
		int j = this.height - Mouse.getY() * this.height
				/ this.mc.displayHeight - 1;
		if (i > guiLeft + 3 && i < guiLeft + 19 && j > guiTop + 3
				&& j < guiTop + 19) {
			List<String> list = new ArrayList<String>();
			double speed = (1.d + tile.getSpeed() * 2.);
			list.add("Speed: " + String.format("%.2f", speed) + "x");
			double effi = (1.d + tile.getEffi() * .75);
			list.add("Efficiency: " + String.format("%.2f", effi) + "x");
			double bonus = (1.d + tile.getBonus() * .25);
			list.add("Bonus: " + String.format("%.2f", bonus) + "x");
			double xp = (1.d + tile.getXp() * .5);
			list.add("XP: " + String.format("%.2f", xp) + "x");
			GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			this.drawHoveringText(list, i, j, fontRendererObj);
			GL11.glPopAttrib();
			GL11.glPopAttrib();
		}
	}

	private void drawMore(int k, int l) {
		if (tile.getSlot() > 0 || true) {
			drawTexturedModalRect(k + 19, l + 47, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47, 176, 31, 18, 18);
		}
		if (tile.getSlot() > 1 || true) {
			drawTexturedModalRect(k + 19, l + 47 + 27, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49 + 27, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43 + 27, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47 + 27, 176, 31, 18, 18);
		}
		int d = 11;
		drawTexturedModalRect(k + 46, l + 103 + d, 176, 0 + d, 14, 14 - d);

	}
}