package mrriegel.furnus.tile;

import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;

import cofh.api.energy.IEnergyReceiver;
import mrriegel.furnus.gui.ContainerDevice;
import mrriegel.furnus.init.ModItems;
import mrriegel.limelib.LimeLib;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.tile.CommonTileInventory;
import mrriegel.limelib.util.EnergyStorageExt;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLever;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

public abstract class TileDevice extends CommonTileInventory implements ITickable, ISidedInventory, IEnergyReceiver {

	private EnergyStorageExt energy = new EnergyStorageExt(80000, 2000);
	private Map<String, Map<Direction, String>> map = Maps.newHashMap();
	protected Map<Integer, Integer> progress = Maps.newHashMap();
	private boolean split;

	public enum Upgrade {
		SPEED(8), EFFICIENCY(8), IO(1), SLOT(2), XP(8), ECO(1), ENERGY(1);
		public final int maxStacksize;

		private Upgrade(int maxStacksize) {
			this.maxStacksize = maxStacksize;
		}
	}

	public enum Direction {
		BOTTOM, TOP, FRONT, BACK, RIGHT, LEFT;

		public EnumFacing face;

		private Direction() {
			face = EnumFacing.VALUES[this.ordinal()];
		}
	}

	public TileDevice() {
		super(13);
		for (int i = 0; i < 3; i++)
			progress.put(i, 0);
		map.put("in", Maps.newHashMap());
		map.put("out", Maps.newHashMap());
		map.put("fuel", Maps.newHashMap());
		for (Direction f : Direction.values())
			for (String k : map.keySet()) {
				map.get(k).put(f, "X");
			}
		map.get("in").put(Direction.TOP, "O");
		map.get("out").put(Direction.BOTTOM, "O");
		map.get("fuel").put(Direction.FRONT, "O");
		map.get("fuel").put(Direction.LEFT, "O");
		map.get("fuel").put(Direction.RIGHT, "O");
		map.get("fuel").put(Direction.BACK, "O");
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

	public int[] getOutputSlots() {
		int s = getAmount(Upgrade.SLOT);
		if (s == 0)
			return new int[] { 3 };
		if (s == 1)
			return new int[] { 3, 4 };
		if (s == 2)
			return new int[] { 3, 4, 5 };
		return new int[0];
	}

	public int[] getFuelSlots() {
		return new int[] { 6, 7 };
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		energy.setEnergyStored(compound.getInteger("energy"));
		progress = NBTHelper.getMap(compound, "progress", Integer.class, Integer.class);
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("energy", energy.getEnergyStored());
		NBTHelper.setMap(compound, "progress", progress);
		return super.writeToNBT(compound);
	}

	@Override
	public abstract boolean openGUI(EntityPlayerMP player);

	public boolean isSplit() {
		return split;
	}

	public void setSplit(boolean split) {
		this.split = split;
	}

	public Map<Integer, Integer> getProgress() {
		return progress;
	}

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
		return getAmount(Upgrade.ENERGY) > 0;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return getAmount(Upgrade.ENERGY) > 0 ? energy.receiveEnergy(maxReceive, simulate) : 0;
	}

	private Direction getDirectionFromSide(EnumFacing side) {
		if (side.getAxis().isVertical())
			return Direction.values()[side.ordinal()];
		EnumFacing face = getBlockState().getValue(BlockDirectional.FACING);
//		System.out.println(getBlockState().getPropertyKeys());
//		System.out.println(BlockHorizontal.FACING.getClass());
//		getBlockState().getPropertyKeys().stream().map(p->p.getClass()).forEach(p->System.out.println(p));
//		System.out.println(getBlockState().getPropertyKeys().stream().map(p->p.getClass()==BlockHorizontal.FACING.getClass()).collect(Collectors.toList()));
//		if("".isEmpty())return Direction.TOP;
		if (face == EnumFacing.NORTH)
			return Direction.values()[side.ordinal()];
		if (face == EnumFacing.SOUTH)
			return Direction.values()[side.getOpposite().ordinal()];
		if (face == EnumFacing.EAST)
			return Direction.values()[side.rotateY().ordinal()];
		if (face == EnumFacing.WEST)
			return Direction.values()[side.rotateYCCW().ordinal()];
		return null;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (getAmount(Upgrade.IO) == 0) {
			switch (side) {
			case DOWN:
				return getOutputSlots();
			case UP:
				return getInputSlots();
			case EAST:
			case NORTH:
			case SOUTH:
			case WEST:
				return getFuelSlots();
			}
		}
		Direction dir = getDirectionFromSide(side);
		int ret[] = new int[] {};
		if (!map.get("in").get(dir).equals("X"))
			ret = Ints.concat(ret, getInputSlots());
		if (!map.get("out").get(dir).equals("X"))
			ret = Ints.concat(ret, getOutputSlots());
		if (!map.get("fuel").get(dir).equals("X"))
			ret = Ints.concat(ret, getFuelSlots());
		return ret;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing side) {
		Direction dir = getDirectionFromSide(side);
		if ((!map.get("in").get(dir).equals("x") && Ints.contains(getInputSlots(), index)) || (!map.get("fuel").get(dir).equals("x") && Ints.contains(getFuelSlots(), index)))
			return isItemValidForSlot(index, itemStackIn);
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing side) {
		Direction dir = getDirectionFromSide(side);
		if ((!map.get("out").get(dir).equals("x") && Ints.contains(getInputSlots(), index)) || (!map.get("fuel").get(dir).equals("x") && Ints.contains(getFuelSlots(), index) && !TileEntityFurnace.isItemFuel(stack)))
			return true;
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (stack.isEmpty() || Ints.contains(getOutputSlots(), index))
			return false;
		if (Ints.contains(getInputSlots(), index))
			return !getResult(stack).isEmpty();
		if (Ints.contains(getFuelSlots(), index))
			return TileEntityFurnace.isItemFuel(stack);
		return stack.getItem() == ModItems.upgrade && ContainerDevice.slotForUpgrade(Upgrade.values()[stack.getItemDamage()], this) != -1;
	}

	public abstract ItemStack getResult(ItemStack input);

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt) {
		int id = nbt.getInteger("id");
		if (id == 0)
			split ^= true;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return super.hasCapability(capability, facing) || (getAmount(Upgrade.ENERGY) > 0 && (capability == CapabilityEnergy.ENERGY || (LimeLib.teslaLoaded && (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER))));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (getAmount(Upgrade.ENERGY) > 0) {
			if (capability == CapabilityEnergy.ENERGY)
				return (T) energy;
			if (LimeLib.teslaLoaded && (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER))
				return (T) new TeslaWrapper(energy);
		}
		return super.getCapability(capability, facing);
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
