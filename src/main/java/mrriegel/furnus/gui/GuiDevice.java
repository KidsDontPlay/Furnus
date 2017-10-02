package mrriegel.furnus.gui;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import com.google.common.collect.Lists;

import mrriegel.furnus.init.ModConfig;
import mrriegel.furnus.tile.TileDevice;
import mrriegel.furnus.util.Enums;
import mrriegel.furnus.util.Enums.Mode;
import mrriegel.furnus.util.Enums.Upgrade;
import mrriegel.limelib.gui.CommonGuiContainer;
import mrriegel.limelib.gui.GuiDrawer;
import mrriegel.limelib.gui.GuiDrawer.Direction;
import mrriegel.limelib.gui.button.CommonGuiButton;
import mrriegel.limelib.gui.button.CommonGuiButton.Design;
import mrriegel.limelib.gui.element.AbstractSlot;
import mrriegel.limelib.gui.element.AbstractSlot.ItemSlot;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GuiDevice extends CommonGuiContainer {

	TileDevice tile;
	String window = null;

	public GuiDevice(ContainerDevice inventorySlotsIn) {
		super(inventorySlotsIn);
		tile = inventorySlotsIn.getTile();
		ySize = 210;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new CommonGuiButton(0, guiLeft + 21, guiTop + 107, 12, 12, "").setTooltip("Split items even").setDesign(Design.SIMPLE));
		//IO
		buttonList.add(new CommonGuiButton(1, guiLeft + 115, guiTop + 106, 12, 12, "I").setTooltip("Input").setDesign(Design.SIMPLE).setButtonColor(0xFF921909));
		buttonList.add(new CommonGuiButton(2, guiLeft + 135, guiTop + 106, 12, 12, "O").setTooltip("Output").setDesign(Design.SIMPLE).setButtonColor(0xFF1A8AEB));
		buttonList.add(new CommonGuiButton(3, guiLeft + 155, guiTop + 106, 12, 12, "F").setTooltip("Fuel").setDesign(Design.SIMPLE).setButtonColor(0xFF000000));
		//Sides
		buttonList.add(new CommonGuiButton(10, guiLeft - 42, guiTop + 44, 14, 14, "0").setDesign(Design.SIMPLE));
		buttonList.add(new CommonGuiButton(11, guiLeft - 42, guiTop + 14, 14, 14, "1").setDesign(Design.SIMPLE));
		buttonList.add(new CommonGuiButton(12, guiLeft - 42, guiTop + 29, 14, 14, "2").setDesign(Design.SIMPLE));
		buttonList.add(new CommonGuiButton(13, guiLeft - 27, guiTop + 44, 14, 14, "3").setDesign(Design.SIMPLE));
		buttonList.add(new CommonGuiButton(14, guiLeft - 27, guiTop + 29, 14, 14, "4").setDesign(Design.SIMPLE));
		buttonList.add(new CommonGuiButton(15, guiLeft - 57, guiTop + 29, 14, 14, "5").setDesign(Design.SIMPLE));
		buttonList.add(new CommonGuiButton(100, guiLeft - 18, guiTop + 5, 10, 9, "x").setDesign(Design.SIMPLE));
		for (GuiButton b : buttonList)
			b.visible = false;
		ItemSlot is = new AbstractSlot.ItemSlot(new ItemStack(Items.BOOK), 0, guiLeft + 130, guiTop + 7, 1, drawer, false, false, false, false);
		elementList.add(is);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (isPointInRegion(40, 106, 15, 15, mouseX, mouseY)) {
			double seconds = tile.getLastTickFuelUsed() <= 0. ? 0. : (tile.getFuel() / tile.getLastTickFuelUsed()) / 20.;
			String ss = String.format(seconds > 1.5 ? "%.0f" : "%.1f", seconds) + " Seconds";
			GuiDrawer.renderToolTip(Lists.newArrayList(ss), mouseX, mouseY);
		}
		if (isPointInRegion(130, 7, 16, 16, mouseX, mouseY)) {
			List<String> strings = Lists.newArrayList();
			strings.add("Speed: " + String.format("%.2f", 1. + tile.getAmount(Upgrade.SPEED) * ModConfig.speedMultiplier) + "x");
			strings.add("Fuel consumption: " + String.format("%.2f", tile.fuelMultiplier()) + "x");
			strings.add("XP: " + String.format("%.2f", 1 + tile.getAmount(Upgrade.XP) * 1.5) + "x");
			if (tile.getAmount(Upgrade.ENERGY) > 0)
				strings.add("Energy: " + tile.getEnergyStored(null) + "/" + tile.getMaxEnergyStored(null));
			GuiDrawer.renderToolTip(strings, mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		drawer.drawBackgroundTexture();
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		drawer.drawPlayerSlots(7, 127);
		drawProgressUnit(18, 0);
		if (tile.getAmount(Upgrade.SLOT) >= 1)
			drawProgressUnit(43, 1);
		if (tile.getAmount(Upgrade.SLOT) >= 2)
			drawProgressUnit(68, 2);
		drawer.drawSlots(56, 103, 2, 1);
		drawer.drawFlame(40, 106, (float) tile.getFuel() / (float) tile.getMaxfuel());
		drawer.drawSlots(151, 7, 1, 5);
		drawWindow();
		drawer.drawColoredRectangle(57, 104, 16, 16, 0x66000000);
		drawer.drawColoredRectangle(75, 104, 16, 16, 0x66000000);
	}

	private void drawWindow() {
		if (window != null) {
			drawer.drawBackgroundTexture(-65, 0, 63, 63);
		}

	}

	private void drawProgressUnit(int y, int index) {
		drawer.drawSlot(20, y);
		drawer.drawProgressArrow(52, y + 2, tile.getProgress().get(index) / (float) tile.neededTicks(), Direction.RIGHT);
		drawer.drawSizedSlot(90, y - 2, 22);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(tile.getBlockType().getLocalizedName(), 6, 6, 4210752);
		if (window != null)
			this.fontRenderer.drawString(WordUtils.capitalize(window), -59, 6, 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	private GuiButton getbyID(int id) {
		return buttonList.stream().filter(b -> b.id == id).findFirst().orElse(null);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		buttonList.get(0).visible = tile.getAmount(Upgrade.SLOT) > 0;
		buttonList.get(0).displayString = tile.isSplit() ? "x" : "";
		boolean io = tile.getAmount(Upgrade.IO) > 0;
		buttonList.get(1).visible = io;
		buttonList.get(2).visible = io;
		buttonList.get(3).visible = io;
		getbyID(100).visible = io && window != null;
		for (int i = 10; i <= 15; i++) {
			getbyID(i).visible = io && window != null;
			if (window != null) {
				Mode m = tile.getMap().get(window).get(Enums.Direction.values()[i - 10]);
				//				getbyID(i).displayString = m.digit;
				getbyID(i).displayString = "";
				int color = 0;
				if (m == Mode.ENABLED)
					color = 0xFF7CFC00;
				else if (m == Mode.DISABLED)
					color = 0xFFEE2C2C;
				else if (m == Mode.AUTO)
					color = 0xFF00EEEE;
				((CommonGuiButton) getbyID(i)).setButtonColor(color);
				((CommonGuiButton) getbyID(i)).setTooltip(Enums.Direction.values()[i - 10] + ": " + m.name().toLowerCase());
			}
		}
		if (!io) {
			window = null;
		}
		ItemSlot is = (ItemSlot) elementList.get(0);
		is.stack = new ItemStack(is.isMouseOver(GuiDrawer.getMouseX(), GuiDrawer.getMouseY()) ? Items.WRITTEN_BOOK : Items.BOOK);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("id", button.id);
		if (button.id == 0) {
			tile.sendMessage(nbt);
			tile.handleMessage(mc.player, nbt);
		} else if (button.id >= 10 && button.id <= 15) {
			nbt.setString("win", window);
			tile.sendMessage(nbt);
			tile.handleMessage(mc.player, nbt);
		} else if (button.id == 1)
			window = "in";
		else if (button.id == 2)
			window = "out";
		else if (button.id == 3)
			window = "fuel";
		else if (button.id == 100)
			window = null;
	}

	public static class Furnus extends GuiDevice {

		public Furnus(ContainerDevice inventorySlotsIn) {
			super(inventorySlotsIn);
		}
	}

	public static class Pulvus extends GuiDevice {

		public Pulvus(ContainerDevice inventorySlotsIn) {
			super(inventorySlotsIn);
		}
	}

}
