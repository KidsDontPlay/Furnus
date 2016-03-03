package mrriegel.furnus.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public abstract class CrunchTEInventory extends TileEntity implements IInventory {

	public final int INVSIZE;
	protected ItemStack[] inv;
	protected final int stackLimit;

	public CrunchTEInventory(int size, int stackLimit) {
		this.INVSIZE = size;
		inv = new ItemStack[size];
		this.stackLimit = stackLimit;
	}

	public CrunchTEInventory(int size) {
		this(size, 64);
	}

	public ItemStack[] getInv() {
		return inv;
	}

	public void clear() {
		inv = new ItemStack[INVSIZE];
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagList invList = tag.getTagList("crunchTE", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < invList.tagCount(); i++) {
			NBTTagCompound stackTag = invList.getCompoundTagAt(i);
			int slot = stackTag.getByte("Slot");

			if (slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(stackTag);
			}
		}
		readSyncableDataFromNBT(tag);
	}

	protected abstract void readSyncableDataFromNBT(NBTTagCompound tag);

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		NBTTagList invList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setByte("Slot", (byte) i);
				inv[i].writeToNBT(stackTag);
				invList.appendTag(stackTag);
			}
		}
		tag.setTag("crunchTE", invList);
		writeSyncableDataToNBT(tag);
	}

	protected abstract void writeSyncableDataToNBT(NBTTagCompound tag);

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound syncData = new NBTTagCompound();
		this.writeSyncableDataToNBT(syncData);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readSyncableDataFromNBT(pkt.func_148857_g());
	}

	@Override
	public int getSizeInventory() {
		return INVSIZE;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int size) {
		if (this.inv[slot] != null) {
			ItemStack itemstack;

			if (this.inv[slot].stackSize <= size) {
				itemstack = this.inv[slot];
				this.inv[slot] = null;
				this.markDirty();
				return itemstack;
			} else {
				itemstack = this.inv[slot].splitStack(size);

				if (this.inv[slot].stackSize == 0) {
					this.inv[slot] = null;
				}

				this.markDirty();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.inv[slot] != null) {
			ItemStack itemstack = this.inv[slot];
			this.inv[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inv[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
		this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return worldObj.getBlock(xCoord, yCoord, zCoord).getLocalizedName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return this.stackLimit;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : p_70300_1_.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;

	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}
}
