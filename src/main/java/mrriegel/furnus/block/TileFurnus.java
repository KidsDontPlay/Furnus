package mrriegel.furnus.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.gui.IOFGui;
import mrriegel.furnus.gui.IOFGui.Mode;
import mrriegel.furnus.gui.UpgradeSlot;
import mrriegel.furnus.item.ItemUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileFurnus extends CrunchTEInventory implements ISidedInventory {
	private boolean burning, eco, inout, split;
	private int speed, effi, slots, bonus, xp, process, fuel;
	Map<Integer, IOFGui.Mode> input, output, fuelput;
	final ArrayList<Integer> outputSlots = new ArrayList<Integer>(
			Arrays.asList(new Integer[] { 3, 4, 5, 6, 7, 8 }));
	final ArrayList<Integer> inputSlots = new ArrayList<Integer>(
			Arrays.asList(new Integer[] { 0, 1, 2 }));
	String face;

	public TileFurnus() {
		super(15);
		input = new HashMap<Integer, IOFGui.Mode>();
		output = new HashMap<Integer, IOFGui.Mode>();
		fuelput = new HashMap<Integer, IOFGui.Mode>();
		for (int i = 0; i < 6; i++) {
			input.put(i, IOFGui.Mode.NORMAL);
			output.put(i, IOFGui.Mode.NORMAL);
			fuelput.put(i, IOFGui.Mode.NORMAL);
		}
	}

	@Override
	protected void readSyncableDataFromNBT(NBTTagCompound tag) {
		burning = tag.getBoolean("burning");
		eco = tag.getBoolean("eco");
		inout = tag.getBoolean("inout");
		split = tag.getBoolean("split");
		speed = tag.getInteger("speed");
		effi = tag.getInteger("effi");
		slots = tag.getInteger("slot");
		bonus = tag.getInteger("bonus");
		xp = tag.getInteger("xp");
		process = tag.getInteger("process");
		fuel = tag.getInteger("fuel");
		input = new Gson().fromJson(tag.getString("input"),
				new TypeToken<Map<Integer, IOFGui.Mode>>() {
				}.getType());
		output = new Gson().fromJson(tag.getString("output"),
				new TypeToken<Map<Integer, IOFGui.Mode>>() {
				}.getType());
		fuelput = new Gson().fromJson(tag.getString("fuelput"),
				new TypeToken<Map<Integer, IOFGui.Mode>>() {
				}.getType());
		face = tag.getString("face");
	}

	@Override
	protected void writeSyncableDataToNBT(NBTTagCompound tag) {
		tag.setBoolean("burning", burning);
		tag.setBoolean("eco", eco);
		tag.setBoolean("inout", inout);
		tag.setBoolean("split", split);
		tag.setInteger("speed", speed);
		tag.setInteger("effi", effi);
		tag.setInteger("slot", slots);
		tag.setInteger("bonus", bonus);
		tag.setInteger("xp", xp);
		tag.setInteger("process", process);
		tag.setInteger("fuel", fuel);
		tag.setString("input", new Gson().toJson(input));
		tag.setString("output", new Gson().toJson(output));
		tag.setString("fuelput", new Gson().toJson(fuelput));
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

	public int getProcess() {
		return process;
	}

	public void setProcess(int process) {
		this.process = process;
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public Map<Integer, IOFGui.Mode> getInput() {
		return input;
	}

	public void setInput(Map<Integer, IOFGui.Mode> input) {
		this.input = input;
	}

	public Map<Integer, IOFGui.Mode> getOutput() {
		return output;
	}

	public void setOutput(Map<Integer, IOFGui.Mode> output) {
		this.output = output;
	}

	public Map<Integer, IOFGui.Mode> getFuelput() {
		return fuelput;
	}

	public void setFuelput(Map<Integer, IOFGui.Mode> fuelput) {
		this.fuelput = fuelput;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot >= 0 && slot <= 8)
			return stack.getItem() != ItemUpgrade.upgrade;
		if (slot == 9)
			return TileEntityFurnace.isItemFuel(stack);
		if (slot >= 10)
			return stack.getItem() == ItemUpgrade.upgrade
					&& UpgradeSlot.in(stack, slot, this);
		return false;
	}

	public void updateStats(EntityPlayer player) {
		int s = getSlots();
		Map<Integer, Integer> upgrades = new HashMap<Integer, Integer>();
		for (int i = 0; i < 7; i++)
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
			}
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		if (s <= getSlots())
			return;
		ArrayList<Integer> lis = new ArrayList<Integer>();
		lis.addAll(Arrays.asList(new Integer[] { 2, 5, 8 }));
		if (getSlots() == 0)
			lis.addAll(Arrays.asList(new Integer[] { 1, 4, 7 }));
		if (worldObj.isRemote)
			return;
		for (int i : lis) {
			if (getStackInSlot(i) == null)
				continue;
			ItemStack stack = getStackInSlot(i).copy();
			player.dropPlayerItemWithRandomChoice(stack, false);
			setInventorySlotContents(i, null);

		}

	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		ArrayList<Integer> lis = new ArrayList<Integer>();
		int wrongSide = getWrongSide(side);
		if (input.get(wrongSide) == Mode.NORMAL
				|| input.get(wrongSide) == Mode.AUTO)
			for (int i : inputSlots)
				lis.add(i);
		if (output.get(wrongSide) == Mode.NORMAL
				|| output.get(wrongSide) == Mode.AUTO)
			for (int i : outputSlots)
				lis.add(i);
		if (fuelput.get(wrongSide) == Mode.NORMAL
				|| fuelput.get(wrongSide) == Mode.AUTO)
			lis.add(9);
		int[] end = new int[lis.size()];
		for (int i = 0; i < lis.size(); i++)
			end[i] = lis.get(i);
		return end;
		// return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
		// };
	}

	int getWrongSide(int side) {
		if (side == 1)
			return 0;
		if (side == 0)
			return 4;
		if (face.equals("N")) {
			switch (side) {
			case 2:
				return 1;
			case 3:
				return 5;
			case 4:
				return 3;
			case 5:
				return 2;
			}
		}
		if (face.equals("S")) {
			switch (side) {
			case 2:
				return 5;
			case 3:
				return 1;
			case 4:
				return 2;
			case 5:
				return 3;
			}
		}
		if (face.equals("E")) {
			switch (side) {
			case 2:
				return 3;
			case 3:
				return 2;
			case 4:
				return 5;
			case 5:
				return 1;
			}
		}
		if (face.equals("W")) {
			switch (side) {
			case 2:
				return 2;
			case 3:
				return 3;
			case 4:
				return 1;
			case 5:
				return 5;
			}
		}
		return 33;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if (!inputSlots.contains(slot) || slot != 9)
			return false;
		ArrayList<Integer> lis = new ArrayList<Integer>();
		int wrongSide = getWrongSide(side);
		if (input.get(wrongSide) == Mode.NORMAL
				|| input.get(wrongSide) == Mode.AUTO)
			for (int i : inputSlots)
				lis.add(i);
		if (fuelput.get(wrongSide) == Mode.NORMAL
				|| fuelput.get(wrongSide) == Mode.AUTO)
			lis.add(9);
		if (!lis.contains(side))
			return false;
		return inStackValid(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if (!outputSlots.contains(slot))
			return false;
		ArrayList<Integer> lis = new ArrayList<Integer>();
		int wrongSide = getWrongSide(side);
		if (output.get(wrongSide) == Mode.NORMAL
				|| output.get(wrongSide) == Mode.AUTO)
			for (int i : outputSlots)
				lis.add(i);
		if (!lis.contains(side))
			return false;
		return inStackValid(slot, stack);
	}

	private boolean contains(int z, int[] a) {
		for (int i : a)
			if (z == i)
				return true;
		return false;
	}

	private boolean inStackValid(int slot, ItemStack stack) {
		if (slot >= 0 && slot <= 2)
			return stack.getItem() != ItemUpgrade.upgrade;
		if (slot == 9)
			return TileEntityFurnace.isItemFuel(stack);
		return false;
	}

	private boolean outStackValid(int slot, ItemStack stack) {
		if (slot >= 3 && slot <= 8)
			return true;
		return false;
	}

	private void output() {
		int size = getOutputSlots().size();
		if (worldObj.getTotalWorldTime() % 20 == 0)
			System.out.println("more");
	}

	private ArrayList<Integer> getOutputSlots() {
		if (slots == 2)
			return outputSlots;
		if (slots == 1)
			return new ArrayList<Integer>(Arrays.asList(new Integer[] { 3, 4,
					6, 7 }));
		else
			return new ArrayList<Integer>(Arrays.asList(new Integer[] { 3, 6 }));
	}

	private ArrayList<Integer> getInputSlots() {
		if (slots == 2)
			return inputSlots;
		if (slots == 1)
			return new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1 }));
		else
			return new ArrayList<Integer>(Arrays.asList(new Integer[] { 0 }));
	}

	@Override
	public void updateEntity() {
		// output
		ArrayList<ItemStack> out = new ArrayList<ItemStack>();
		output();
		split();
		// if (worldObj.getTotalWorldTime() % 30 == 0)
		// System.out.println(getInput());
		for (int i : getOutputSlots()) {

		}
	}

	private void split() {
		if (slots == 0 || !split)
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
		List<ItemStack> lis=new ArrayList<ItemStack>();
		for(Integer i:getInputSlots()){
			// if
			// lis.add(gets)
		}
	}
}
