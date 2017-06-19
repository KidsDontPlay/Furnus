package mrriegel.furnus.gui;

import java.io.IOException;

import mrriegel.furnus.tile.TileDevice;
import mrriegel.furnus.tile.TileDevice.Upgrade;
import mrriegel.limelib.gui.CommonGuiContainer;
import mrriegel.limelib.gui.GuiDrawer.Direction;
import mrriegel.limelib.gui.button.CommonGuiButton;
import mrriegel.limelib.gui.button.CommonGuiButton.Design;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;

public class GuiDevice extends CommonGuiContainer {

	TileDevice tile;

	public GuiDevice(ContainerDevice inventorySlotsIn) {
		super(inventorySlotsIn);
		tile = inventorySlotsIn.getTile();
		ySize = 210;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new CommonGuiButton(0, guiLeft + 21, guiTop + 100, 12, 12, "").setTooltip("Split items even").setDesign(Design.SIMPLE));
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
		if (tile.getAmount(Upgrade.SLOT) == 1)
			drawProgressUnit(43);
		if (tile.getAmount(Upgrade.SLOT) == 2)
			drawProgressUnit(68);
		drawer.drawSlots(56, 103, 2, 1);
		drawer.drawFlame(40, 106, .8f);
		drawer.drawSlots(151, 7, 1, 5);
	}

	private void drawProgressUnit(int y) {
		drawer.drawSlot(20, y);
		drawer.drawProgressArrow(52, y + 2, 0.2f, Direction.RIGHT);
		drawer.drawSlot(92, y);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(tile.getBlockType().getLocalizedName(), 6, 6, 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		buttonList.get(0).displayString = tile.isSplit() ? "x" : "";
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("id", button.id);
		if (button.id == 0) {
			tile.sendMessage(nbt);
			tile.handleMessage(mc.player, nbt);
		}
	}

}
