package mrriegel.furnus.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.gui.UpgradeSlot;
import mrriegel.furnus.item.ItemUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileFurnus extends CrunchTEInventory implements ISidedInventory {
	private boolean burning, eco, inout, split;
	private int speed, effi, slots, bonus, xp, process, fuel;
	Map<Integer, SideConfig> map;

	public TileFurnus() {
		super(15);
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
		slots = tag.getInteger("slot");
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
		tag.setInteger("slot", slots);
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

	public void updateStats() {
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
			// player.dropPlayerItemWithRandomChoice(stack, false);
			setInventorySlotContents(i, null);

		}

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
		if (slot == 9)
			return TileEntityFurnace.isItemFuel(stack);
		return false;
	}

	private boolean outStackValid(int slot, ItemStack stack) {
		if (slot >= 3 && slot <= 8)
			return true;
		return false;
	}

}
