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
		return ItemStack.EMPTY;
	}

	public float getExperience(ItemStack stack) {
		for (Entry<ItemStack, Float> entry : experienceList.entrySet()) {
			ItemStack in = entry.getKey().copy();
			if (in.isItemEqual(stack) || (in.getItem() == stack.getItem() && in.getItemDamage() == OreDictionary.WILDCARD_VALUE))
				return entry.getValue();
		}
		return 0f;
	}

	public void addBlock(Block block, ItemStack stack, float exp) {
		this.addItem(Item.getItemFromBlock(block), stack, exp);
	}

	public void addItem(Item item, ItemStack stack, float exp) {
		this.addItemStack(new ItemStack(item, 1, 32767), stack, exp);
	}

	public void addItemStack(ItemStack in, ItemStack out, float exp) {
		if (in.isEmpty() || out.isEmpty())
			return;
		for (ItemStack stack : crushingList.keySet())
			if (in.isItemEqual(stack))
				return;
		this.crushingList.put(in.copy(), out);
		this.experienceList.put(out, Float.valueOf(exp));
	}
}