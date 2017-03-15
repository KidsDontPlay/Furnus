package mrriegel.furnus.block;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.handler.ConfigHandler;
import mrriegel.limelib.helper.InvHelper;
import mrriegel.limelib.helper.StackHelper;
import mrriegel.limelib.tile.CommonTileInventory;
import mrriegel.limelib.util.EnergyStorageExt;
import mrriegel.limelib.util.Utils;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.item.IExoflameHeatable;
import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler.IExternalHeatable;
import cofh.api.energy.IEnergyReceiver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

//@Optional.Interface(iface = "blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler$IExternalHeatable", modid = "immersiveengineering")
public abstract class AbstractMachine extends CommonTileInventory implements ISidedInventory, ITickable, IExoflameHeatable, IEnergyReceiver, IExternalHeatable {

	protected boolean burning, eco, inout, split, rf;
	protected int speed, effi, slots, bonus, xp, fuel, maxFuel, remainTicks;
	protected Map<Direction, Mode> input = Maps.newHashMap(), output = Maps.newHashMap(), fuelput = Maps.newHashMap();
	protected Map<Integer, Integer> progress;

	public EnergyStorageExt en = new EnergyStorageExt(64000, Integer.MAX_VALUE - 10, 0);

	public AbstractMachine() {
		super(15);
		for (Direction f : Direction.values()) {
			input.put(f, Mode.X);
			output.put(f, Mode.X);
			fuelput.put(f, Mode.X);
		}
		input.put(Direction.TOP, Mode.ENABLED);
		for (Direction f : Direction.values())
			if (f != Direction.TOP && f != Direction.BOTTOM)
				fuelput.put(f, Mode.ENABLED);
		output.put(Direction.BOTTOM, Mode.ENABLED);
		progress = Maps.newHashMap();
		for (int i = 0; i < 3; i++)
			progress.put(i, 0);
	}

	public enum Mode {
		ENABLED, AUTO, X;
		protected static Mode[] vals = values();

		public Mode next() {
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}

	public enum Direction {
		TOP, FRONT, LEFT, RIGHT, BOTTOM, BACK;
	}

	@SuppressWarnings("serial")
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		burning = compound.getBoolean("burning");
		eco = compound.getBoolean("eco");
		inout = compound.getBoolean("inout");
		split = compound.getBoolean("split");
		rf = compound.getBoolean("rf");
		speed = compound.getInteger("speed");
		effi = compound.getInteger("effi");
		slots = compound.getInteger("slot");
		bonus = compound.getInteger("bonus");
		xp = compound.getInteger("xp");
		fuel = compound.getInteger("fuel");
		maxFuel = compound.getInteger("maxFuel");
		input = new Gson().fromJson(compound.getString("input"), new TypeToken<Map<Direction, Mode>>() {
		}.getType());
		output = new Gson().fromJson(compound.getString("output"), new TypeToken<Map<Direction, Mode>>() {
		}.getType());
		fuelput = new Gson().fromJson(compound.getString("fuelput"), new TypeToken<Map<Direction, Mode>>() {
		}.getType());
		progress = new Gson().fromJson(compound.getString("progress"), new TypeToken<Map<Integer, Integer>>() {
		}.getType());
		super.readFromNBT(compound);
		NBTTagList invList = compound.getTagList("crunchTE", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < invList.tagCount(); i++) {
			NBTTagCompound stackTag = invList.getCompoundTagAt(i);
			int slot = stackTag.getByte("Slot");
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("burning", burning);
		compound.setBoolean("eco", eco);
		compound.setBoolean("inout", inout);
		compound.setBoolean("split", split);
		compound.setBoolean("rf", rf);
		compound.setInteger("speed", speed);
		compound.setInteger("effi", effi);
		compound.setInteger("slot", slots);
		compound.setInteger("bonus", bonus);
		compound.setInteger("xp", xp);
		compound.setInteger("fuel", fuel);
		compound.setInteger("maxFuel", maxFuel);
		compound.setString("input", new Gson().toJson(input));
		compound.setString("output", new Gson().toJson(output));
		compound.setString("fuelput", new Gson().toJson(fuelput));
		compound.setString("progress", new Gson().toJson(progress));
		super.writeToNBT(compound);
		NBTTagList invList = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i) != null) {
				NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setByte("Slot", (byte) i);
				getStackInSlot(i).writeToNBT(stackTag);
				invList.appendTag(stackTag);
			}
		}
		compound.setTag("crunchTE", invList);
		return compound;
	}

	@Override
	public abstract boolean openGUI(EntityPlayerMP player);

	public boolean isBurning() {
		return burning;
	}

	public boolean isEco() {
		return eco;
	}

	public boolean isInout() {
		return inout;
	}

	public boolean isSplit() {
		return split;
	}

	public boolean isRf() {
		return rf;
	}

	public int getSpeed() {
		return speed;
	}

	public int getEffi() {
		return effi;
	}

	public int getSlots() {
		return slots;
	}

	public int getBonus() {
		return bonus;
	}

	public int getXp() {
		return xp;
	}

	public int getFuel() {
		return fuel;
	}

	public int getMaxFuel() {
		return maxFuel;
	}

	public Map<Integer, Integer> getProgress() {
		return progress;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		Direction wrongSide = getWrongSide(side);
		if (!inout) {
			if (side == EnumFacing.UP)
				return Ints.toArray(getInputSlots());
			if (side == EnumFacing.DOWN)
				return Ints.toArray(getOutputSlots());
			return new int[] { 9 };
		}
		List<Integer> lis = Lists.newArrayList();
		if (input.get(wrongSide) != Mode.X)
			lis.addAll(getInputSlots());
		if (output.get(wrongSide) != Mode.X)
			lis.addAll(getOutputSlots());
		if (fuelput.get(wrongSide) != Mode.X)
			lis.add(9);
		return Ints.toArray(lis);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		if (!getInputSlots().contains(slot) && slot != 9)
			return false;
		Direction wrongSide = getWrongSide(side);
		if ((input.get(wrongSide) != Mode.X) && getInputSlots().contains(slot)) {
			return isItemValidForSlot(slot, stack);
		}
		if (fuelput.get(wrongSide) != Mode.X && slot == 9)
			return isItemValidForSlot(slot, stack);
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		if (!getOutputSlots().contains(slot) && slot != 9)
			return false;
		Direction wrongSide = getWrongSide(side);
		if ((output.get(wrongSide) != Mode.X) && getOutputSlots().contains(slot)) {
			return true;
		}
		if (fuelput.get(wrongSide) != Mode.X && slot == 9 && !TileEntityFurnace.isItemFuel(stack))
			return true;
		return false;
	}

	protected List<Integer> getOutputSlots() {
		if (slots == 2)
			return Lists.newArrayList(3, 4, 5, 6, 7, 8);
		if (slots == 1)
			return Lists.newArrayList(3, 4, 6, 7);
		else
			return Lists.newArrayList(3, 6);
	}

	protected List<Integer> getInputSlots() {
		if (slots == 2)
			return Lists.newArrayList(0, 1, 2);
		if (slots == 1)
			return Lists.newArrayList(0, 1);
		else
			return Lists.newArrayList(0);
	}

	@Override
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	public void updateStats(EntityPlayer player) {
		int s = getSlots();
		Map<Integer, Integer> upgrades = Maps.newHashMap();
		for (int i = 0; i < 8; i++)
			upgrades.put(i, 0);
		for (int i = 10; i < 15; i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack == null)
				continue;
			int meta = stack.getItemDamage();
			upgrades.put(meta, stack.stackSize);
		}
		for (Entry<Integer, Integer> e : upgrades.entrySet()) {
			switch (e.getKey()) {
			case 0:
				speed = ConfigHandler.speed ? e.getValue() : 0;
				break;
			case 1:
				effi = ConfigHandler.effi ? e.getValue() : 0;
				break;
			case 2:
				inout = ConfigHandler.io ? e.getValue() > 0 ? true : false : false;
				break;
			case 3:
				slots = ConfigHandler.slot ? e.getValue() : 0;
				break;
			case 4:
				bonus = ConfigHandler.bonus ? e.getValue() : 0;
				break;
			case 5:
				xp = ConfigHandler.xp ? e.getValue() : 0;
				break;
			case 6:
				eco = ConfigHandler.eco ? e.getValue() > 0 ? true : false : false;
				break;
			case 7:
				rf = ConfigHandler.rf ? e.getValue() > 0 ? true : false : false;
				break;
			}
		}

		if (s > getSlots()) {
			List<Integer> lis = Lists.newArrayList(2, 5, 8);
			if (getSlots() == 0)
				lis.addAll(Lists.newArrayList(1, 4, 7));
			for (int i : lis) {
				if (getStackInSlot(i) == null)
					continue;
				ItemStack stack = getStackInSlot(i).copy();
				player.dropItem(stack, false);
				setInventorySlotContents(i, null);

			}
		}
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 8);

	}

	@Override
	public void update() {
		//		if (world.isRemote) {
		//			return;
		//		}
		output();
		input();
		split();
		move();
		if (fuel > maxFuel)
			maxFuel = fuel;
		if (fuel < 0)
			fuel = 0;
		if (world.getTotalWorldTime() % 5 == 0) {
			if (fuel > 0 && !burning) {
				burning = true;
				((AbstractBlock<?>) getBlockType()).setState(world, getPos(), world.getBlockState(getPos()), burning);
			} else if (fuel <= 0 && burning) {
				burning = false;
				((AbstractBlock<?>) getBlockType()).setState(world, getPos(), world.getBlockState(getPos()), burning);
			}
		}

		for (int i = 0; i <= speed * ConfigHandler.speedMulti; i++) {
			burn(0);
			if (slots >= 2)
				burn(2);
			if (slots >= 1)
				burn(1);
		}

	}

	public int neededTicks() {
		return 160;
	}

	protected void fuelUp(int slot) {
		if (fuel >= 1 || !canProcess(slot))
			return;
		int fuelTime = 0;
		if (getStackInSlot(9) != null && TileEntityFurnace.isItemFuel(getStackInSlot(9))) {
			fuelTime = TileEntityFurnace.getItemBurnTime(getStackInSlot(9)) * 100;
			if (getStackInSlot(9).getItem().getContainerItem(getStackInSlot(9)) == null)
				decrStackSize(9, 1);
			else
				setInventorySlotContents(9, getStackInSlot(9).getItem().getContainerItem(getStackInSlot(9)));
		} else {
			int fac = ConfigHandler.RF;
			fuelTime = (int) ((consumeRF(1600 * fac) / (double) fac) * 100);
		}
		fuelTime *= (neededTicks() / 200.) + .01;
		fuel += fuelTime;
		maxFuel = fuelTime;
		markForSync();
	}

	protected void burn(int slot) {
		if (getStackInSlot(slot) == null)
			if (progress.get(slot) != 0)
				progress.put(slot, 0);
		fuelUp(slot);

		boolean progressed = false;
		if (fuel > 0) {
			if (canProcess(slot)) {
				progress.put(slot, progress.get(slot) + 1);
				progressed = true;
				if (progress.get(slot) >= neededTicks()) {
					if (!world.isRemote)
						processItem(slot);
					markForSync();
					progress.put(slot, 0);
				}
			}
		} else {
			if (!eco)
				progress.put(slot, 0);
		}
		if (fuel > 0 && (progressed || (!progressing(slot) && !eco))) {
			if (canProcessAny() || slot == 0)
				fuel -= getCalc() * 100;
		}
		calculateTicks();
		//		sendMessage();
	}

	private void calculateTicks() {
		if (fuel <= 0)
			remainTicks = 0;
		int ticks = (fuel / 100);
		ticks /= (speed * ConfigHandler.speedMulti + 1);
		ticks /= Math.max(1, processes());
		ticks /= getCalc();
		remainTicks = ticks;
	}

	public double getCalc() {
		return (getSpeed() * (ConfigHandler.speedFuelMulti / 10.) + getBonus() * (ConfigHandler.bonusFuelMulti / 10.) + 1.) / (getEffi() * (ConfigHandler.effiMulti / 10.) + 1.);
	}

	boolean progressing(int slot) {
		for (int i = 0; i < 3; i++) {
			if (i == slot)
				continue;
			if (canProcess(i))
				return true;
		}
		return false;
	}

	protected abstract void processItem(int slot);

	protected abstract boolean canProcess(int slot);

	protected void output() {
		if (!inout || world.getTotalWorldTime() % 50 / (speed + slots + 1) != 0)
			return;
		for (TileEntity t : getIInventories()) {
			for (int i : getOutputSlots()) {
				if (getStackInSlot(i) == null || output.get(getWrongSide(getDirection(this, t))) != Mode.AUTO)
					continue;
				IItemHandler ir = InvHelper.getItemHandler(t, getDirection(this, t).getOpposite());
				int num = getStackInSlot(i).stackSize;
				ItemStack res = ItemHandlerHelper.insertItem(ir, getStackInSlot(i).copy(), false);
				int rest = res == null ? 0 : res.stackSize;
				if (num == rest)
					continue;
				setInventorySlotContents(i, rest > 0 ? ItemHandlerHelper.copyStackWithSize(getStackInSlot(i).copy(), rest) : null);
				break;
			}
		}
	}

	protected void input() {
		if (!inout || world.getTotalWorldTime() % 60 / (speed + slots + 1) != 0)
			return;
		for (TileEntity t : getIInventories()) {
			if (input.get(getWrongSide(getDirection(this, t))) != Mode.AUTO && fuelput.get(getWrongSide(getDirection(this, t))) != Mode.AUTO)
				continue;
			IItemHandler ir = InvHelper.getItemHandler(t, getDirection(this, t).getOpposite());
			IItemHandler that = InvHelper.getItemHandler(this, getDirection(this, t));
			for (int i = 0; i < ir.getSlots(); i++) {
				if (ir.getStackInSlot(i) == null)
					continue;
				int num = ir.getStackInSlot(i).stackSize;
				ItemStack res = ItemHandlerHelper.insertItem(that, ir.getStackInSlot(i).copy(), true);
				int rest = res == null ? 0 : res.stackSize;
				if (num == rest)
					continue;
				int inserted = num - rest;
				ItemStack exed = ir.extractItem(i, inserted, false);
				if (exed == null)
					continue;
				ItemHandlerHelper.insertItem(that, exed.copy(), false);
				break;
			}
		}
	}

	protected EnumFacing getDirection(TileEntity von, TileEntity zu) {
		if (von.getPos().getY() == zu.getPos().getY() + 1)
			return EnumFacing.DOWN;
		if (von.getPos().getY() == zu.getPos().getY() - 1)
			return EnumFacing.UP;
		if (von.getPos().getZ() == zu.getPos().getZ() + 1)
			return EnumFacing.NORTH;
		if (von.getPos().getZ() == zu.getPos().getZ() - 1)
			return EnumFacing.SOUTH;
		if (von.getPos().getX() == zu.getPos().getX() + 1)
			return EnumFacing.WEST;
		if (von.getPos().getX() == zu.getPos().getX() - 1)
			return EnumFacing.EAST;
		return null;
	}

	protected List<TileEntity> getIInventories() {
		List<TileEntity> lis = Lists.newArrayList();
		for (EnumFacing face : EnumFacing.values()) {
			BlockPos p = getPos().offset(face);
			if (InvHelper.hasItemHandler(world, p, face.getOpposite()))
				lis.add(world.getTileEntity(p));
		}
		return lis;
	}

	@SuppressWarnings("incomplete-switch")
	Direction getWrongSide(EnumFacing side) {
		if (side == EnumFacing.UP)
			return Direction.TOP;
		if (side == EnumFacing.DOWN)
			return Direction.BOTTOM;
		EnumFacing face = world.getBlockState(pos).getValue(BlockHorizontal.FACING);
		if (face == EnumFacing.NORTH) {
			switch (side) {
			case NORTH:
				return Direction.FRONT;
			case SOUTH:
				return Direction.BACK;
			case WEST:
				return Direction.RIGHT;
			case EAST:
				return Direction.LEFT;
			}
		}
		if (face == EnumFacing.SOUTH) {
			switch (side) {
			case NORTH:
				return Direction.BACK;
			case SOUTH:
				return Direction.FRONT;
			case WEST:
				return Direction.LEFT;
			case EAST:
				return Direction.RIGHT;
			}
		}
		if (face == EnumFacing.EAST) {
			switch (side) {
			case NORTH:
				return Direction.RIGHT;
			case SOUTH:
				return Direction.LEFT;
			case WEST:
				return Direction.BACK;
			case EAST:
				return Direction.FRONT;
			}
		}
		if (face == EnumFacing.WEST) {
			switch (side) {
			case NORTH:
				return Direction.LEFT;
			case SOUTH:
				return Direction.RIGHT;
			case WEST:
				return Direction.FRONT;
			case EAST:
				return Direction.BACK;
			}
		}
		return null;
	}

	protected void split() {
		if (slots == 0 || !split || world.getTotalWorldTime() % 4 != 0)
			return;
		boolean x = false;
		for (int i : getInputSlots()) {
			if (getStackInSlot(i) != null) {
				x = true;
				break;
			}
		}
		if (!x)
			return;
		for (int i : getInputSlots()) {
			for (int j : getInputSlots())
				if (i >= j)
					tryMerge(j, i);
		}

	}

	protected void move() {
		if (slots == 0 || split || world.getTotalWorldTime() % 5 != 0)
			return;
		for (int i : getInputSlots()) {
			for (int j : getInputSlots())
				if (getStackInSlot(j) == null && getStackInSlot(i) != null && !canProcess(i) && fit(getStackInSlot(i), j)) {
					setInventorySlotContents(j, getStackInSlot(i).copy());
					setInventorySlotContents(i, null);
				}
		}
	}

	protected abstract boolean fit(ItemStack stack, int slot);

	protected void tryMerge(int i1, int i2) {
		ItemStack stack1 = getStackInSlot(i1), stack2 = getStackInSlot(i2);
		if (stack1 == null && stack2 == null)
			return;
		if (stack1 == null) {
			if (stack2.stackSize <= 1 || !fit(stack2, i1))
				return;
			List<ItemStack> splitted = StackHelper.split(stack2);
			setInventorySlotContents(i1, splitted.get(0));
			setInventorySlotContents(i2, splitted.get(1));
			return;
		} else if (stack2 == null) {
			if (stack1.stackSize <= 1 || !fit(stack1, i2))
				return;
			List<ItemStack> splitted = StackHelper.split(stack1);
			setInventorySlotContents(i1, splitted.get(0));
			setInventorySlotContents(i2, splitted.get(1));
			return;
		} else {
			if (ItemHandlerHelper.canItemStacksStack(stack1, stack2)) {
				int s = stack1.stackSize + stack2.stackSize;
				setInventorySlotContents(i1, ItemHandlerHelper.copyStackWithSize(stack1, Utils.split(s, 2).get(0)));
				setInventorySlotContents(i2, ItemHandlerHelper.copyStackWithSize(stack1, Utils.split(s, 2).get(1)));
				return;
			}
		}
	}

	boolean canProcessAny() {
		for (int i : getInputSlots()) {
			if (canProcess(i))
				return true;
		}
		return false;
	}

	int processes() {
		int processes = 0;
		for (int i : getInputSlots()) {
			if (canProcess(i))
				processes++;
		}
		return processes;
	}

	@Override
	public boolean canSmelt() {
		if (world.isRemote)
			return false;
		return canProcessAny();
	}

	@Override
	public int getBurnTime() {
		return remainTicks;
	}

	@Override
	public void boostBurnTime() {
		if (!canProcessAny() || fuel > 20000)
			return;
		fuel += 20000 - fuel;
		maxFuel = 20000;
		markForSync();
	}

	@Override
	public void boostCookTime() {
	}

	@Override
	public int doHeatTick(int energyAvailable, boolean redstone) {
		final double multiplier = 0.08;
		if (energyAvailable <= 0 || redstone || !canProcessAny() || remainTicks > 4)
			return 0;
		int consume = /*energyAvailable;
						while (consume > 256)
						consume--;*/
		MathHelper.clamp(energyAvailable, 0, 256);
		fuel += consume / multiplier;
		maxFuel = fuel;
		markForSync();
		return consume;
	}

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt) {
		switch (nbt.getInteger("id")) {
		case 0:
			split ^= true;
			break;
		case 1:
			getMap(nbt.getString("kind")).put(Direction.values()[nbt.getInteger("dir")], getMap(nbt.getString("kind")).get(Direction.values()[nbt.getInteger("dir")]).next());
			break;
		case 2:
			player.openGui(Furnus.instance, nbt.getInteger("gui"), world, getX(), getY(), getZ());
		default:
			break;
		}
	}

	boolean consumeRF(int num, boolean simulate) {
		int value = num;
		if (en.getEnergyStored() < value)
			return false;
		if (!simulate) {
			en.modifyEnergyStored(-value);
		}
		return true;
	}

	int consumeRF(int max) {
		int m = Math.min(max, en.getEnergyStored());
		en.modifyEnergyStored(-m);
		return m;
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return en.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return en.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return isRf();
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return en.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (isRf() && capability == CapabilityEnergy.ENERGY) || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (isRf() && capability == CapabilityEnergy.ENERGY) {
			return (T) en;
		}
		return super.getCapability(capability, facing);
	}

	public Map<Direction, Mode> getMap(String id) {
		if (id.equals("I"))
			return input;
		if (id.equals("O"))
			return output;
		if (id.equals("F"))
			return fuelput;
		return null;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this || isInvalid() ? false : player.getDistanceSq(getX() + 0.5D, getY() + 0.5D, getZ() + 0.5D) <= 64.0D;
	}
}
