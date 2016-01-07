package mrriegel.furnus.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mrriegel.furnus.InventoryHelper;
import mrriegel.furnus.handler.ConfigurationHandler;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.message.ProgressMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.item.IExoflameHeatable;
import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler.IExternalHeatable;
import cofh.api.energy.IEnergyReceiver;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public abstract class AbstractMachine extends CrunchTEInventory implements ISidedInventory,
		IExternalHeatable, IExoflameHeatable, IEnergyReceiver {

	public AbstractMachine(int size) {
		super(size);
	}

	protected boolean burning, eco, inout, split, rf;
	protected int speed, effi, slots, bonus, xp, fuel, maxFuel, remainTicks;
	protected Map<Direction, Mode> input, output, fuelput;
	protected Map<Integer, Integer> progress;
	protected String face;

	public AbstractMachine() {
		super(15);
		input = new HashMap<Direction, Mode>();
		output = new HashMap<Direction, Mode>();
		fuelput = new HashMap<Direction, Mode>();
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
		progress = new HashMap<Integer, Integer>();
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

	@Override
	protected void readSyncableDataFromNBT(NBTTagCompound tag) {
		burning = tag.getBoolean("burning");
		eco = tag.getBoolean("eco");
		inout = tag.getBoolean("inout");
		split = tag.getBoolean("split");
		rf = tag.getBoolean("rf");
		speed = tag.getInteger("speed");
		effi = tag.getInteger("effi");
		slots = tag.getInteger("slot");
		bonus = tag.getInteger("bonus");
		xp = tag.getInteger("xp");
		fuel = tag.getInteger("fuel");
		maxFuel = tag.getInteger("maxFuel");
		remainTicks = tag.getInteger("remainTicks");
		input = new Gson().fromJson(tag.getString("input"), new TypeToken<Map<Direction, Mode>>() {
		}.getType());
		output = new Gson().fromJson(tag.getString("output"),
				new TypeToken<Map<Direction, Mode>>() {
				}.getType());
		fuelput = new Gson().fromJson(tag.getString("fuelput"),
				new TypeToken<Map<Direction, Mode>>() {
				}.getType());
		progress = new Gson().fromJson(tag.getString("progress"),
				new TypeToken<Map<Integer, Integer>>() {
				}.getType());
		face = tag.getString("face");
	}

	@Override
	protected void writeSyncableDataToNBT(NBTTagCompound tag) {
		tag.setBoolean("burning", burning);
		tag.setBoolean("eco", eco);
		tag.setBoolean("inout", inout);
		tag.setBoolean("split", split);
		tag.setBoolean("rf", rf);
		tag.setInteger("speed", speed);
		tag.setInteger("effi", effi);
		tag.setInteger("slot", slots);
		tag.setInteger("bonus", bonus);
		tag.setInteger("xp", xp);
		tag.setInteger("fuel", fuel);
		tag.setInteger("maxFuel", maxFuel);
		tag.setInteger("remainTicks", remainTicks);
		tag.setString("input", new Gson().toJson(input));
		tag.setString("output", new Gson().toJson(output));
		tag.setString("fuelput", new Gson().toJson(fuelput));
		tag.setString("progress", new Gson().toJson(progress));
		tag.setString("face", face);
	}

	public boolean isBurning() {
		return burning;
	}

	public void setBurning(boolean burning) {
		this.burning = burning;
	}

	public boolean isEco() {
		return eco;
	}

	public void setEco(boolean eco) {
		this.eco = eco;
	}

	public boolean isInout() {
		return inout;
	}

	public void setInout(boolean inout) {
		this.inout = inout;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getEffi() {
		return effi;
	}

	public void setEffi(int effi) {
		this.effi = effi;
	}

	public int getSlots() {
		return slots;
	}

	public void setSlots(int slots) {
		this.slots = slots;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public boolean isSplit() {
		return split;
	}

	public void setSplit(boolean split) {
		this.split = split;
	}

	public boolean isRF() {
		return rf;
	}

	public void setRF(boolean rf) {
		this.rf = rf;
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public int getMaxFuel() {
		return maxFuel;
	}

	public void setMaxFuel(int maxFuel) {
		this.maxFuel = maxFuel;
	}

	public int getRemainTicks() {
		return remainTicks;
	}

	public void setRemainTicks(int remainTicks) {
		this.remainTicks = remainTicks;
	}

	public Map<Direction, Mode> getInput() {
		return input;
	}

	public void setInput(Map<Direction, Mode> input) {
		this.input = input;
	}

	public Map<Direction, Mode> getOutput() {
		return output;
	}

	public void setOutput(Map<Direction, Mode> output) {
		this.output = output;
	}

	public Map<Direction, Mode> getFuelput() {
		return fuelput;
	}

	public void setFuelput(Map<Direction, Mode> fuelput) {
		this.fuelput = fuelput;
	}

	public Map<Integer, Integer> getProgress() {
		return progress;
	}

	public void setProgress(Map<Integer, Integer> progress) {
		this.progress = progress;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if (!inout)
			return new int[] {};
		ArrayList<Integer> lis = new ArrayList<Integer>();
		Direction wrongSide = getWrongSide(side);
		if (input.get(wrongSide) != Mode.X)
			lis.addAll(getInputSlots());
		if (output.get(wrongSide) != Mode.X)
			lis.addAll(getOutputSlots());
		if (fuelput.get(wrongSide) != Mode.X)
			lis.add(9);
		int[] end = new int[lis.size()];
		for (int i = 0; i < lis.size(); i++)
			end[i] = lis.get(i);
		return end;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if (!inout)
			return false;
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
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if (!inout)
			return false;
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

	protected ArrayList<Integer> getOutputSlots() {
		if (slots == 2)
			return new ArrayList<Integer>(Arrays.asList(new Integer[] { 3, 4, 5, 6, 7, 8 }));
		if (slots == 1)
			return new ArrayList<Integer>(Arrays.asList(new Integer[] { 3, 4, 6, 7 }));
		else
			return new ArrayList<Integer>(Arrays.asList(new Integer[] { 3, 6 }));
	}

	protected ArrayList<Integer> getInputSlots() {
		if (slots == 2)
			return new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2 }));
		if (slots == 1)
			return new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1 }));
		else
			return new ArrayList<Integer>(Arrays.asList(new Integer[] { 0 }));
	}

	@Override
	public abstract boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_);

	public void updateStats(EntityPlayer player) {
		int s = getSlots();
		Map<Integer, Integer> upgrades = new HashMap<Integer, Integer>();
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
				setSpeed(e.getValue());
				break;
			case 1:
				setEffi(e.getValue());
				break;
			case 2:
				setInout(e.getValue() > 0 ? true : false);
				break;
			case 3:
				setSlots(e.getValue());
				break;
			case 4:
				setBonus(e.getValue());
				break;
			case 5:
				setXp(e.getValue());
				break;
			case 6:
				setEco(e.getValue() > 0 ? true : false);
				break;
			case 7:
				setRF(e.getValue() > 0 ? true : false);
				break;
			}
		}

		if (s <= getSlots())
			return;
		ArrayList<Integer> lis = new ArrayList<Integer>();
		lis.addAll(Arrays.asList(new Integer[] { 2, 5, 8 }));
		if (getSlots() == 0)
			lis.addAll(Arrays.asList(new Integer[] { 1, 4, 7 }));
		for (int i : lis) {
			if (getStackInSlot(i) == null)
				continue;
			ItemStack stack = getStackInSlot(i).copy();
			player.dropPlayerItemWithRandomChoice(stack, false);
			setInventorySlotContents(i, null);

		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			return;
		}
		output();
		input();
		split();
		move();
		if (fuel > 0 && !burning) {
			burning = true;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		} else if (fuel <= 0 && burning) {
			burning = false;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		for (int i = 0; i <= speed * ConfigurationHandler.speedMulti; i++) {
			burn(0);
			if (slots >= 2)
				burn(2);
			if (slots >= 1)
				burn(1);
		}

	}

	protected void fuelUp(int slot) {
		if (fuel >= 1 || getStackInSlot(9) == null
				|| !TileEntityFurnace.isItemFuel(getStackInSlot(9)) || !canProcess(slot))
			return;
		int fuelTime = TileEntityFurnace.getItemBurnTime(getStackInSlot(9)) * 100;
		fuel += fuelTime;
		maxFuel = fuelTime;
		if (getStackInSlot(9).getItem().getContainerItem(getStackInSlot(9)) == null)
			InventoryHelper.decrStackSize(this, 9, 1);
		else
			setInventorySlotContents(9,
					getStackInSlot(9).getItem().getContainerItem(getStackInSlot(9)));
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
				if (progress.get(slot) >= 200) {
					processItem(slot);
					progress.put(slot, 0);
				}
			}
		} else {
			if (!eco)
				progress.put(slot, 0);
		}
		if (fuel > 0 && (progressed || (!progressing(slot) && !eco))) {
			double effi = (getSpeed() * (ConfigurationHandler.speedFuelMulti / 10.) + getBonus()
					* (ConfigurationHandler.bonusFuelMulti / 10.) + 1.)
					/ (getEffi() * (ConfigurationHandler.effiMulti / 10.) + 1.);
			fuel -= effi * 100;
		}
		calculateTicks();
		sendMessage();
	}

	private void calculateTicks() {
		if (fuel <= 0)
			remainTicks = 0;
		int ticks = (fuel / 100);
		ticks /= (speed * ConfigurationHandler.speedMulti + 1);
		ticks /= (slots + 1);
		double effi = (getSpeed() * (ConfigurationHandler.speedFuelMulti / 10.) + getBonus()
				* (ConfigurationHandler.bonusFuelMulti / 10.) + 1.)
				/ (getEffi() * (ConfigurationHandler.effiMulti / 10.) + 1.);
		ticks /= effi;
		remainTicks = ticks;
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

	void sendMessage() {
		PacketHandler.INSTANCE.sendToAllAround(new ProgressMessage(burning, xCoord, yCoord, zCoord,
				fuel, maxFuel, progress), new TargetPoint(worldObj.provider.dimensionId, xCoord,
				yCoord, zCoord, 12));
	}

	protected abstract void processItem(int slot);

	protected abstract boolean canProcess(int slot);

	protected void output() {
		if (!inout || worldObj.getTotalWorldTime() % 50 / (speed + slots + 1) != 0)
			return;
		for (IInventory ir : getIInventories()) {
			for (int i : getOutputSlots()) {
				if (getStackInSlot(i) == null
						|| output.get(getWrongSide(getDirection(this, (TileEntity) ir))) != Mode.AUTO)
					continue;

				if (!(ir instanceof ISidedInventory)) {
					int num = getStackInSlot(i).stackSize;
					int rest = InventoryHelper.addToInventoryWithLeftover(getStackInSlot(i).copy(),
							ir, false);
					if (num == rest)
						continue;
					setInventorySlotContents(i,
							rest > 0 ? InventoryHelper.copyStack(getStackInSlot(i).copy(), rest)
									: null);
					TileEntity inv = (TileEntity) ir;
					worldObj.markBlockForUpdate(inv.xCoord, inv.yCoord, inv.zCoord);
					break;
				} else if (ir instanceof ISidedInventory) {
					int num = getStackInSlot(i).stackSize;
					int rest = InventoryHelper.addToSidedInventoryWithLeftover(getStackInSlot(i)
							.copy(), (ISidedInventory) ir, getDirection((TileEntity) ir, this),
							false);
					if (num == rest)
						continue;
					setInventorySlotContents(i,
							rest > 0 ? InventoryHelper.copyStack(getStackInSlot(i).copy(), rest)
									: null);
					TileEntity inv = (TileEntity) ir;
					worldObj.markBlockForUpdate(inv.xCoord, inv.yCoord, inv.zCoord);
					break;
				}
			}
		}
	}

	protected void input() {
		if (!inout || worldObj.getTotalWorldTime() % 60 / (speed + slots + 1) != 0)
			return;
		for (IInventory ir : getIInventories()) {
			if (input.get(getWrongSide(getDirection(this, (TileEntity) ir))) != Mode.AUTO
					&& fuelput.get(getWrongSide(getDirection(this, (TileEntity) ir))) != Mode.AUTO)
				continue;
			int side = getDirection(this, (TileEntity) ir);
			if (!(ir instanceof ISidedInventory)) {
				for (int i = 0; i < ir.getSizeInventory(); i++) {
					if (ir.getStackInSlot(i) == null)
						continue;
					int num = ir.getStackInSlot(i).stackSize;
					int rest = InventoryHelper.addToSidedInventoryWithLeftover(ir.getStackInSlot(i)
							.copy(), this, side, false);
					if (num == rest)
						continue;
					ir.setInventorySlotContents(i,
							rest > 0 ? InventoryHelper.copyStack(ir.getStackInSlot(i).copy(), rest)
									: null);
					TileEntity inv = (TileEntity) ir;
					worldObj.markBlockForUpdate(inv.xCoord, inv.yCoord, inv.zCoord);
					break;
				}
			} else if (ir instanceof ISidedInventory) {
				for (int i : ((ISidedInventory) ir).getAccessibleSlotsFromSide(side)) {
					if (ir.getStackInSlot(i) == null)
						continue;
					if (!((ISidedInventory) ir).canExtractItem(i, ir.getStackInSlot(i),
							getDirection((TileEntity) ir, this)))
						break;
					int num = ir.getStackInSlot(i).stackSize;
					int rest = InventoryHelper.addToSidedInventoryWithLeftover(ir.getStackInSlot(i)
							.copy(), this, side, false);
					if (num == rest)
						continue;
					ir.setInventorySlotContents(i,
							rest > 0 ? InventoryHelper.copyStack(ir.getStackInSlot(i).copy(), rest)
									: null);
					TileEntity inv = (TileEntity) ir;
					worldObj.markBlockForUpdate(inv.xCoord, inv.yCoord, inv.zCoord);
					break;
				}
			}
		}
	}

	protected int getDirection(TileEntity von, TileEntity zu) {
		if (von.yCoord == zu.yCoord + 1)
			return 0;
		if (von.yCoord == zu.yCoord - 1)
			return 1;
		if (von.zCoord == zu.zCoord + 1)
			return 2;
		if (von.zCoord == zu.zCoord - 1)
			return 3;
		if (von.xCoord == zu.xCoord + 1)
			return 4;
		if (von.xCoord == zu.xCoord - 1)
			return 5;
		return -1;
	}

	protected List<IInventory> getIInventories() {
		ArrayList<IInventory> lis = new ArrayList<IInventory>();
		TileEntity a = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
		if (a != null && a instanceof IInventory)
			lis.add((IInventory) a);

		TileEntity b = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
		if (b != null && b instanceof IInventory)
			lis.add((IInventory) b);

		TileEntity c = worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
		if (c != null && c instanceof IInventory)
			lis.add((IInventory) c);

		TileEntity d = worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
		if (d != null && d instanceof IInventory)
			lis.add((IInventory) d);

		TileEntity e = worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
		if (e != null && e instanceof IInventory)
			lis.add((IInventory) e);

		TileEntity f = worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
		if (f != null && f instanceof IInventory)
			lis.add((IInventory) f);

		return lis;
	}

	Direction getWrongSide(int side) {
		if (side == 1)
			return Direction.TOP;
		if (side == 0)
			return Direction.BOTTOM;
		if (face.equals("N")) {
			switch (side) {
			case 2:
				return Direction.FRONT;
			case 3:
				return Direction.BACK;
			case 4:
				return Direction.RIGHT;
			case 5:
				return Direction.LEFT;
			}
		}
		if (face.equals("S")) {
			switch (side) {
			case 2:
				return Direction.BACK;
			case 3:
				return Direction.FRONT;
			case 4:
				return Direction.LEFT;
			case 5:
				return Direction.RIGHT;
			}
		}
		if (face.equals("E")) {
			switch (side) {
			case 2:
				return Direction.RIGHT;
			case 3:
				return Direction.LEFT;
			case 4:
				return Direction.BACK;
			case 5:
				return Direction.FRONT;
			}
		}
		if (face.equals("W")) {
			switch (side) {
			case 2:
				return Direction.LEFT;
			case 3:
				return Direction.RIGHT;
			case 4:
				return Direction.FRONT;
			case 5:
				return Direction.BACK;
			}
		}
		return null;
	}

	protected void split() {
		if (slots == 0 || !split || worldObj.getTotalWorldTime() % 4 != 0)
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
		if (slots == 0 || split || worldObj.getTotalWorldTime() % 5 != 0)
			return;
		for (int i : getInputSlots()) {
			for (int j : getInputSlots()) {
				if (getStackInSlot(j) == null && getStackInSlot(i) != null && !canProcess(i)
						&& fit(getStackInSlot(i), j)) {
					setInventorySlotContents(j, getStackInSlot(i).copy());
					setInventorySlotContents(i, null);
				}
			}
		}
	}

	public abstract boolean fit(ItemStack stack, int slot);

	protected void tryMerge(int i1, int i2) {
		ItemStack stack1 = getStackInSlot(i1), stack2 = getStackInSlot(i2);
		if (stack1 == null && stack2 == null)
			return;
		if (stack1 == null) {
			if (stack2.stackSize <= 1 || !fit(stack2, i1))
				return;
			stack1 = stack2.copy();
			stack1.stackSize = split(stack2.stackSize)[0];
			stack2.stackSize = split(stack2.stackSize)[1];
			setInventorySlotContents(i1, stack1.stackSize > 0 ? stack1 : null);
			setInventorySlotContents(i2, stack2.stackSize > 0 ? stack2 : null);
			return;
		} else if (stack2 == null) {
			if (stack1.stackSize <= 1 || !fit(stack1, i2))
				return;
			stack2 = stack1.copy();
			stack2.stackSize = split(stack1.stackSize)[0];
			stack1.stackSize = split(stack1.stackSize)[1];
			setInventorySlotContents(i1, stack1.stackSize > 0 ? stack1 : null);
			setInventorySlotContents(i2, stack2.stackSize > 0 ? stack2 : null);
			return;
		} else {
			if (InventoryHelper.areStacksEqual(stack1, stack2, true)) {
				int s = stack1.stackSize + stack2.stackSize;
				stack1.stackSize = split(s)[0];
				stack2.stackSize = split(s)[1];
				setInventorySlotContents(i1, stack1.stackSize > 0 ? stack1 : null);
				setInventorySlotContents(i2, stack2.stackSize > 0 ? stack2 : null);
				return;
			}
		}
	}

	int[] split(int a) {
		if (a % 2 == 0)
			return new int[] { a / 2, a / 2 };
		else
			return new int[] { a / 2 + 1, a / 2 };
	}

	boolean canProcessAny() {
		for (int i : getInputSlots()) {
			if (canProcess(i))
				return true;
		}
		return false;
	}

	@Override
	public int doHeatTick(int energyAvailable, boolean redstone) {
		if (1 == 1)
			return 0;
		if (energyAvailable <= 0 /* || redstone */|| !canProcessAny() || fuel >= maxFuel)
			return 0;
		if (energyAvailable == 1)
			return energyAvailable;
		maxFuel = (int) (30000 * ((getSpeed() * (ConfigurationHandler.speedFuelMulti / 10.)
				+ getBonus() * (ConfigurationHandler.bonusFuelMulti / 10.) + 1.) / (getEffi()
				* (ConfigurationHandler.effiMulti / 10.) + 1.)));
		int f = fuel;
		int need = (maxFuel - f);
		if (need <= 0)
			return 0;
		final double multiplier = 0.08;
		while (need * multiplier > energyAvailable) {
			need--;
		}
		fuel += need;
		return (int) (need * multiplier);
	}

	@Override
	public boolean canSmelt() {
		if (worldObj.isRemote)
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
	}

	@Override
	public void boostCookTime() {
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		if (!canProcessAny() || fuel > maxFuel || simulate || !rf)
			return 0;
		int f = fuel;
		fuel = maxFuel = 60000 * (slots + 1);
		return ((fuel - f) / 12) * speed * ConfigurationHandler.speedMulti;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return 0;
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return rf;
	}
}
