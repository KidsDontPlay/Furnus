package mrriegel.furnus.gui;

import java.io.IOException;
import java.util.List;

import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mrriegel.furnus.Furnus;
import mrriegel.furnus.block.AbstractMachine;
import mrriegel.furnus.block.TileFurnus;
import mrriegel.furnus.block.TilePulvus;
import mrriegel.furnus.handler.ConfigHandler;
import mrriegel.furnus.jei.PulvusJEIPlugin;
import mrriegel.limelib.gui.CommonGuiContainer;
import mrriegel.limelib.helper.NBTHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.common.Loader;

import com.google.common.collect.Lists;

public class MachineGUI extends CommonGuiContainer {
	private static ResourceLocation texture;
	GuiButton i, o, f, check;
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
		buttonList.add(check = new GuiButtonExt(0, guiLeft + 46, guiTop + 4, 14, 14, tile.isSplit() ? "X" : " "));
		buttonList.add(i = new GuiButtonExt(1, guiLeft + 115, guiTop + 108, 14, 14, "I"));
		buttonList.add(o = new GuiButtonExt(2, guiLeft + 134, guiTop + 108, 14, 14, "O"));
		buttonList.add(f = new GuiButtonExt(3, guiLeft + 153, guiTop + 108, 14, 14, "F"));
		if (!tile.isSplit())
			check.visible = false;
		if (!tile.isInout()) {
			i.visible = false;
			o.visible = false;
			f.visible = false;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawMore(guiLeft, guiTop);
		if (tile.getSlots() > 0)
			mc.fontRendererObj.drawString(I18n.format("gui.furnus.split"), guiLeft + 22, guiTop + 7, 4210752);

	}

	@Override
	public void updateScreen() {
		if (tile.getSlots() < 1) {
			check.enabled = false;
			check.visible = false;
		} else {
			check.enabled = true;
			check.visible = true;
		}
		check.displayString = tile.isSplit() ? "X" : " ";
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
		super.updateScreen();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float particleTicks) {
		super.drawScreen(mouseX, mouseY, particleTicks);
		if (mouseX > guiLeft + 3 && mouseX < guiLeft + 19 && mouseY > guiTop + 3 && mouseY < guiTop + 19) {
			List<String> list = Lists.newArrayList();
			double speed = (1.d + tile.getSpeed() * 1.d * ConfigHandler.speedMulti);
			list.add(I18n.format("gui.furnus.speed") + ": " + String.format("%.2f", speed) + "x");
			list.add(I18n.format("gui.furnus.effi") + ": " + String.format("%.2f", tile.getCalc()) + "x");
			int bonus = (tile.getBonus() * (int) (ConfigHandler.bonusMulti * 100.));
			list.add(I18n.format("gui.furnus.bonus") + ": " + String.format("%d", bonus) + "%");
			double xp = (1.d + tile.getXp() * ConfigHandler.xpMulti);
			list.add(I18n.format("gui.furnus.xp") + ": " + String.format("%.2f", xp) + "x");
			if (tile.isRf()) {
				list.add((int) ((ConfigHandler.RF * tile.neededTicks()) * tile.getCalc()) + " RF per Operation");
				list.add("RF: " + tile.en.getEnergyStored() + "/" + tile.en.getMaxEnergyStored());
			}
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			this.drawHoveringText(list, mouseX, mouseY, fontRendererObj);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
		if (mouseX > guiLeft + 45 && mouseX < guiLeft + 45 + 16 && mouseY > guiTop + 102 && mouseY < guiTop + 102 + 16) {
			List<String> list = Lists.newArrayList();
			double seconds = tile.getBurnTime() / 20.;
			if (seconds > 2)
				list.add((int) (seconds) + " Seconds");
			else
				list.add(String.format("%.1f", seconds) + " Seconds");
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			this.drawHoveringText(list, mouseX, mouseY, fontRendererObj);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
		if (mouseX > guiLeft + 40 && mouseX < guiLeft + 65 && mouseY > guiTop + 20 && mouseY < guiTop + 95 && Loader.isModLoaded("JEI")) {
			List<String> list = Lists.newArrayList();
			list.add("Show Recipes");
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			this.drawHoveringText(list, mouseX, mouseY, fontRendererObj);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseX > guiLeft + 40 && mouseX < guiLeft + 65 && mouseY > guiTop + 20 && mouseY < guiTop + 95 && Loader.isModLoaded("JEI")) {
			PulvusJEIPlugin.showCategory(tile instanceof TileFurnus ? VanillaRecipeCategoryUid.SMELTING : Furnus.MODID + ".pulvus");
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
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			tile.sendMessage(NBTHelper.setInt(new NBTTagCompound(), "id", 0));
			tile.handleMessage(mc.player, NBTHelper.setInt(new NBTTagCompound(), "id", 0));
		} else {
			//			mc.thePlayer.closeScreen();
			mc.player.openGui(Furnus.instance, button.id, tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
		}
	}

	private void drawMore(int k, int l) {
		switch (tile.getSlots()) {
		case 0:
			drawTexturedModalRect(k + 19, l + 47, 176, 31, 18, 18);
			drawTexturedModalRect(k + 43, l + 49, 176, 75, 22, 15);
			drawTexturedModalRect(k + 72, l + 43, 176, 49, 26, 26);
			drawTexturedModalRect(k + 106, l + 47, 176, 31, 18, 18);
			drawTexturedModalRect(k + 42, l + 49, 176, 14, (int) (tile.getProgress().get(0) / (tile.neededTicks() / 24.)), 17);
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
			drawTexturedModalRect(k + 42, l + 49 - 13, 176, 14, (int) (tile.getProgress().get(0) / (tile.neededTicks() / 24.)), 17);
			drawTexturedModalRect(k + 42, l + 49 + 14, 176, 14, (int) (tile.getProgress().get(1) / (tile.neededTicks() / 24.)), 17);
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
			drawTexturedModalRect(k + 42, l + 49 - 27, 176, 14, (int) (tile.getProgress().get(0) / (tile.neededTicks() / 24.)), 17);
			drawTexturedModalRect(k + 42, l + 49, 176, 14, (int) (tile.getProgress().get(1) / (tile.neededTicks() / 24.)), 17);
			drawTexturedModalRect(k + 42, l + 49 + 27, 176, 14, (int) (tile.getProgress().get(2) / (tile.neededTicks() / 24.)), 17);
			break;
		}
		int percent = (int) (((float) tile.getFuel()) / ((float) tile.getMaxFuel()) * 100f);
		int d = 13 - ((int) (14 * (percent / 100.0f)));
		drawTexturedModalRect(k + 45, l + 102 + d, 176, 0 + d, 14, 14 - d);
	}

}