package mrriegel.furnus.gui;

import mrriegel.furnus.tile.TileDevice;
import mrriegel.limelib.gui.CommonGuiContainer;
import mrriegel.limelib.gui.GuiDrawer.Direction;

public class GuiDevice extends CommonGuiContainer {

	TileDevice tile;

	public GuiDevice(ContainerDevice inventorySlotsIn) {
		super(inventorySlotsIn);
		tile = inventorySlotsIn.getTile();
		ySize = 210;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		drawer.drawBackgroundTexture();
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		drawer.drawPlayerSlots(7, 127);
		drawProgressUnit(18);
		drawProgressUnit(42);
		drawProgressUnit(68);
//		drawProgressUnit(81);
//		drawProgressUnit(95);
		drawer.drawSlot(40, 103);
		drawer.drawFlame(60, 106, .8f);
	}

	private void drawProgressUnit(int y) {
		drawer.drawSlot(20, y);
		drawer.drawProgressArrow(52, y + 2, 0, Direction.RIGHT);
		drawer.drawSlot(92, y);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(tile.getBlockType().getLocalizedName(), 6, 6, 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

	}

}
