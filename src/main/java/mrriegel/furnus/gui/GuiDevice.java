package mrriegel.furnus.gui;

import java.io.IOException;

import org.apache.commons.lang3.text.WordUtils;

import mrriegel.furnus.tile.TileDevice;
import mrriegel.furnus.util.Enums.Mode;
import mrriegel.furnus.util.Enums.Upgrade;
import mrriegel.limelib.gui.CommonGuiContainer;
import mrriegel.limelib.gui.GuiDrawer.Direction;
import mrriegel.limelib.gui.button.CommonGuiButton;
import mrriegel.limelib.gui.button.CommonGuiButton.Design;
import net.minecraft.client.gui.GuiButton;
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
		buttonList.add(new CommonGuiButton(0, guiLeft + 21, guiTop + 100, 12, 12, "").setTooltip("Split items even").setDesign(Design.SIMPLE));
		buttonList.add(new CommonGuiButton(1, guiLeft + 115, guiTop + 106, 12, 12, "I").setTooltip("Input"));
		buttonList.add(new CommonGuiButton(2, guiLeft + 135, guiTop + 106, 12, 12, "O").setTooltip("Output"));
		buttonList.add(new CommonGuiButton(3, guiLeft + 155, guiTop + 106, 12, 12, "F").setTooltip("Fuel"));
		buttonList.add(new CommonGuiButton(10, guiLeft - 42, guiTop + 44, 14, 14, "0"));
		buttonList.add(new CommonGuiButton(11, guiLeft - 42, guiTop + 14, 14, 14, "1"));
		buttonList.add(new CommonGuiButton(12, guiLeft - 42, guiTop + 29, 14, 14, "2"));
		buttonList.add(new CommonGuiButton(13, guiLeft - 27, guiTop + 44, 14, 14, "3"));
		buttonList.add(new CommonGuiButton(14, guiLeft - 27, guiTop + 29, 14, 14, "4"));
		buttonList.add(new CommonGuiButton(15, guiLeft - 57, guiTop + 29, 14, 14, "5"));
		buttonList.add(new CommonGuiButton(100, guiLeft - 18, guiTop + 5, 10, 9, "x").setDesign(Design.SIMPLE));
		for(GuiButton b:buttonList)
			b.visible=false;
	}
	
	

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		drawer.drawBackgroundTexture();
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		drawer.drawPlayerSlots(7, 127);
		drawProgressUnit(18);
		if (tile.getAmount(Upgrade.SLOT) >= 1)
			drawProgressUnit(43);
		if (tile.getAmount(Upgrade.SLOT) >= 2)
			drawProgressUnit(68);
		drawer.drawSlots(56, 103, 2, 1);
		drawer.drawFlame(40, 106, .8f);
		drawer.drawSlots(151, 7, 1, 5);
		drawWindow();
	}

	private void drawWindow() {
		if (window != null) {
			drawer.drawBackgroundTexture(-65, 0, 63, 63);
		}

	}

	private void drawProgressUnit(int y) {
		drawer.drawSlot(20, y);
		drawer.drawProgressArrow(52, y + 2, 0.2f, Direction.RIGHT);
		drawer.drawSlot(92, y);
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
				Mode m = tile.getMap().get(window).get(mrriegel.furnus.util.Enums.Direction.values()[i - 10]);
				getbyID(i).displayString = m.digit;
				((CommonGuiButton) getbyID(i)).setTooltip(mrriegel.furnus.util.Enums.Direction.values()[i - 10] + ": " + m.name().toLowerCase());
			}
		}
		if (!io) {
			window = null;
		}
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

}
