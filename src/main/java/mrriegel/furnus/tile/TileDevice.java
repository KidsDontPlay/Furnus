package mrriegel.furnus.tile;

import cofh.api.energy.IEnergyReceiver;
import mrriegel.furnus.init.ModItems;
import mrriegel.limelib.tile.CommonTileInventory;
import mrriegel.limelib.util.EnergyStorageExt;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

public abstract class TileDevice extends CommonTileInventory implements ITickable, ISidedInventory, IEnergyReceiver {

	private EnergyStorageExt energy = new EnergyStorageExt(80000, 2000);

	protected static final int[] INSLOTS = new int[] { 0, 2, 4, 6 };
	protected static final int[] OUTSLOTS = new int[] { 1, 3, 5, 6 };

	public enum Upgrade {
		SPEED(8), EFFICIENCY(8), IO(1), SLOT(2), XP(8), ECO(1), ENERGY(1);
		public final int maxStacksize;

		private Upgrade(int maxStacksize) {
			this.maxStacksize = maxStacksize;
		}

	}

	public TileDevice() {
		super(13);
	}

	public int getAmount(Upgrade upgrade) {
		for (int i = 8; i < 13; i++) {
			ItemStack u = getStackInSlot(i);
			if (u.getItem() == ModItems.upgrade && Upgrade.values()[u.getItemDamage()] == upgrade)
				return u.getCount();
		}
		return 0;
	}

	public int[] getInputSlots() {
		int s = getAmount(Upgrade.SLOT);
		if (s == 0)
			return new int[] { 0 };
		if (s == 1)
			return new int[] { 0, 1 };
		if (s == 2)
			return new int[] { 0, 1, 2 };
		return new int[0];
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		energy.setEnergyStored(compound.getInteger("energy"));
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("energy", energy.getEnergyStored());
		return super.writeToNBT(compound);
	}

	@Override
	public abstract boolean openGUI(EntityPlayerMP player);

	@Override
	public int getEnergyStored(EnumFacing from) {
		return energy.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return energy.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return false;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return energy.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Optional.InterfaceList(value = { @Optional.Interface(iface = "net.darkhax.tesla.api.ITeslaHolder", modid = "tesla"), @Optional.Interface(iface = "net.darkhax.tesla.api.ITeslaConsumer", modid = "tesla") })
	private static class TeslaWrapper implements ITeslaHolder, ITeslaConsumer {
		private IEnergyStorage storage;

		public TeslaWrapper(IEnergyStorage storage) {
			this.storage = storage;
		}

		@Override
		public long givePower(long power, boolean simulated) {
			return storage.receiveEnergy((int) (power % Integer.MAX_VALUE), simulated);
		}

		@Override
		public long getStoredPower() {
			return storage.getEnergyStored();
		}

		@Override
		public long getCapacity() {
			return storage.getMaxEnergyStored();
		}

	}

}
