package mrriegel.furnus.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CrunchHandler {
	private static final CrunchHandler base = new CrunchHandler();
	public Map<ItemStack, ItemStack> crushingList = new HashMap<ItemStack, ItemStack>();
	private Map<ItemStack, Float> experienceList = new HashMap<ItemStack, Float>();

	public static CrunchHandler instance() {
		return base;
	}

	private CrunchHandler() {
	}

	public ItemStack getResult(ItemStack stack) {
		for (Entry<ItemStack, ItemStack> entry : crushingList.entrySet()) {
			ItemStack in = entry.getKey().copy();
			if (in.isItemEqual(stack) || (in.getItem() == stack.getItem() && in.getItemDamage() == OreDictionary.WILDCARD_VALUE))
				return entry.getValue().copy();
		}
		return null;
	}

	public float getExperience(ItemStack stack) {
		for (Entry<ItemStack, Float> entry : experienceList.entrySet()) {
			ItemStack in = entry.getKey().copy();
			if (in.isItemEqual(stack) || (in.getItem() == stack.getItem() && in.getItemDamage() == OreDictionary.WILDCARD_VALUE))
				return entry.getValue();
		}
		return 0f;
	}

	public void addBlock(Block p_151393_1_, ItemStack p_151393_2_, float p_151393_3_) {
		this.addItem(Item.getItemFromBlock(p_151393_1_), p_151393_2_, p_151393_3_);
	}

	public void addItem(Item p_151396_1_, ItemStack p_151396_2_, float p_151396_3_) {
		this.addItemStack(new ItemStack(p_151396_1_, 1, 32767), p_151396_2_, p_151396_3_);
	}

	public void addItemStack(ItemStack in, ItemStack out, float p_151394_3_) {
		if (in == null || out == null)
			return;
		for (ItemStack stack : crushingList.keySet())
			if (in.isItemEqual(stack))
				return;
		this.crushingList.put(in.copy(), out);
		this.experienceList.put(out, Float.valueOf(p_151394_3_));
	}
}