package mrriegel.furnus.gui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.init.ModItems;
import mrriegel.furnus.tile.TileDevice;
import mrriegel.furnus.tile.TileFurnus;
import mrriegel.furnus.util.Enums.Upgrade;
import mrriegel.limelib.gui.CommonContainerTileInventory;
import mrriegel.limelib.gui.slot.SlotFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerDevice extends CommonContainerTileInventory<TileDevice> {

	int startSlots;

	public ContainerDevice(InventoryPlayer invPlayer, TileDevice tile) {
		super(invPlayer, tile);
		startSlots = tile.getAmount(Upgrade.SLOT);
	}

	@Override
	protected void initSlots() {
		initPlayerSlots(8, 128);
		int slots = getTile().getAmount(Upgrade.SLOT);
		addSlotToContainer(new SlotFilter(getTile(), 0, 21, 19, s -> getTile().isItemValidForSlot(0, s)));
		if (slots >= 1)
			addSlotToContainer(new SlotFilter(getTile(), 1, 21, 44, s -> getTile().isItemValidForSlot(1, s)));
		if (slots >= 2)
			addSlotToContainer(new SlotFilter(getTile(), 2, 21, 69, s -> getTile().isItemValidForSlot(2, s)));
		addSlotToContainer(new SlotOutput(getPlayer(), getTile(), 3, 93, 19));
		if (slots >= 1)
			addSlotToContainer(new SlotOutput(getPlayer(), getTile(), 4, 93, 44));
		if (slots >= 2)
			addSlotToContainer(new SlotOutput(getPlayer(), getTile(), 5, 93, 69));
		addSlotToContainer(new SlotFilter(getTile(), 6, 57, 104, s -> getTile().isItemValidForSlot(6, s)));
		addSlotToContainer(new SlotFilter(getTile(), 7, 75, 104, s -> getTile().isItemValidForSlot(7, s)));
		addSlotToContainer(new SlotFilter(getTile(), 8, 152, 8, s -> getTile().isItemValidForSlot(8, s)));
		addSlotToContainer(new SlotFilter(getTile(), 9, 152, 8 + 18, s -> getTile().isItemValidForSlot(9, s)));
		addSlotToContainer(new SlotFilter(getTile(), 10, 152, 8 + 36, s -> getTile().isItemValidForSlot(10, s)));
		addSlotToContainer(new SlotFilter(getTile(), 11, 152, 8 + 54, s -> getTile().isItemValidForSlot(11, s)));
		addSlotToContainer(new SlotFilter(getTile(), 12, 152, 8 + 72, s -> getTile().isItemValidForSlot(12, s)));
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!getPlayer().world.isRemote) {
			TileDevice tile = getTile();
			int nowSlots = tile.getAmount(Upgrade.SLOT);
			if (startSlots != nowSlots) {
				ItemStack s = ItemStack.EMPTY;
				if (!invPlayer.getItemStack().isEmpty()) {
					s = invPlayer.getItemStack().copy();
					invPlayer.setItemStack(ItemStack.EMPTY);
				}
				getPlayer().openGui(Furnus.instance, tile instanceof TileFurnus ? 0 : 1, getPlayer().world, tile.getX(), tile.getY(), tile.getZ());
				if (!s.isEmpty()) {
					invPlayer.setItemStack(s);
					((EntityPlayerMP) getPlayer()).connection.sendPacket(new SPacketSetSlot(-1, 0, s));
				}
				if (nowSlots < 2) {
					//					getPlayer().dropItem(tile.removeStackFromSlot(2), false);
					//					getPlayer().dropItem(tile.removeStackFromSlot(5), false);
					InventoryHelper.spawnItemStack(getPlayer().world, getPlayer().posX, getPlayer().posY, getPlayer().posZ, tile.removeStackFromSlot(2));
					InventoryHelper.spawnItemStack(getPlayer().world, getPlayer().posX, getPlayer().posY, getPlayer().posZ, tile.removeStackFromSlot(5));
					//					ItemHandlerHelper.giveItemToPlayer(getPlayer(), tile.removeStackFromSlot(2));
					//					ItemHandlerHelper.giveItemToPlayer(getPlayer(), tile.removeStackFromSlot(5));
				}
				if (nowSlots < 1) {
					//					getPlayer().dropItem(tile.removeStackFromSlot(1), false);
					//					getPlayer().dropItem(tile.removeStackFromSlot(4), false);
					InventoryHelper.spawnItemStack(getPlayer().world, getPlayer().posX, getPlayer().posY, getPlayer().posZ, tile.removeStackFromSlot(1));
					InventoryHelper.spawnItemStack(getPlayer().world, getPlayer().posX, getPlayer().posY, getPlayer().posZ, tile.removeStackFromSlot(4));
					//					ItemHandlerHelper.giveItemToPlayer(getPlayer(), tile.removeStackFromSlot(1));
					//					ItemHandlerHelper.giveItemToPlayer(getPlayer(), tile.removeStackFromSlot(4));
				}
				getPlayer().openContainer.detectAndSendChanges();
			}
		}
	}

	@Override
	protected List<Area> allowedSlots(ItemStack stack, IInventory inv, int index) {
		if (inv != invPlayer)
			return Collections.singletonList(getAreaForEntireInv(invPlayer));
		else {
			List<Area> lis = Lists.newArrayList();
			if (TileEntityFurnace.isItemFuel(stack))
				lis.add(getAreaForInv(getTile(), 6, 2));
			if (getTile().isItemValidForSlot(0, stack))
				lis.add(getAreaForInv(getTile(), 0, 1 + getTile().getAmount(Upgrade.SLOT)));
			for (int i = 8; i < 13; i++)
				if (getTile().isItemValidForSlot(i, stack))
					lis.add(getAreaForInv(getTile(), i, 1));
			return lis;
		}
	}

	public static boolean slotForUpgrade(int index, Upgrade u, TileDevice tile) {
		int emp = -1, up = -1;
		for (int i = Arrays.stream(tile.getFuelSlots()).reduce((a, b) -> b).getAsInt() + 1; i < tile.getSizeInventory(); i++) {
			ItemStack stack = tile.getStackInSlot(i);
			if (stack.isEmpty() && emp == -1)
				emp = i;
			else if (stack.getItem() == ModItems.upgrade && stack.getItemDamage() == u.ordinal())
				up = i;
		}
		return up == index || (up == -1 && emp != -1);
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		playerIn.world.notifyNeighborsOfStateChange(getTile().getPos(), getTile().getBlockType(), false);
	}

}
