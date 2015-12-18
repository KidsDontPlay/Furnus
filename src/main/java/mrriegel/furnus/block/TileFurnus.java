package mrriegel.furnus.block;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileFurnus extends CrunchTEInventory implements ISidedInventory {
	private boolean burning, eco, inout, split;
	private int speed, effi, slot, bonus, xp, process, fuel;
	Map<Integer, SideConfig> map;

	public TileFurnus() {
		super(10);
		map = new HashMap<Integer, SideConfig>();
	}

	@Override
	protected void readSyncableDataFromNBT(NBTTagCompound tag) {
		burning = tag.getBoolean("burning");
		eco = tag.getBoolean("eco");
		inout = tag.getBoolean("inout");
		split = tag.getBoolean("split");
		speed = tag.getInteger("speed");
		effi = tag.getInteger("effi");
		slot = tag.getInteger("slot");
		bonus = tag.getInteger("bonus");
		xp = tag.getInteger("xp");
		process = tag.getInteger("process");
		fuel = tag.getInteger("fuel");
		map = new Gson().fromJson(tag.getString("map"),
				new TypeToken<HashMap<Integer, SideConfig>>() {
				}.getType());
	}

	@Override
	protected void writeSyncableDataToNBT(NBTTagCompound tag) {
		tag.setBoolean("burning", burning);
		tag.setBoolean("eco", eco);
		tag.setBoolean("inout", inout);
		tag.setBoolean("split", split);
		tag.setInteger("speed", speed);
		tag.setInteger("effi", effi);
		tag.setInteger("slot", slot);
		tag.setInteger("bonus", bonus);
		tag.setInteger("xp", xp);
		tag.setInteger("process", process);
		tag.setInteger("fuel", fuel);
		tag.setString("map", new Gson().toJson(map));
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

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
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

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return map.get(side).getSlots();
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if (!map.get(side).isIn())
			return false;
		if (!contains(slot, map.get(side).getSlots()))
			return false;
		return inStackValid(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if (!map.get(side).isOut())
			return false;
		if (!contains(slot, map.get(side).getSlots()))
			return false;
		return outStackValid(slot, stack);
	}

	private boolean contains(int z, int[] a) {
		for (int i : a)
			if (z == i)
				return true;
		return false;
	}

	private boolean inStackValid(int slot, ItemStack stack) {
		if (slot >= 0 && slot <= 2)
			return true;
		if (slot >= 3 && slot <= 8)
			return false;
		if (slot == 9)
			return GameRegistry.getFuelValue(stack) > 0;
		return false;
	}

	private boolean outStackValid(int slot, ItemStack stack) {
		if (slot >= 0 && slot <= 2)
			return false;
		if (slot >= 3 && slot <= 8)
			return true;
		return false;
	}

}
