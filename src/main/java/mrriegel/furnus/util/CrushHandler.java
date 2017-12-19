package mrriegel.furnus.util;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CrushHandler {
	private static final CrushHandler base = new CrushHandler();
	public Map<ItemStack, ItemStack> crushingList = Maps.newHashMap();
	private Map<ItemStack, Float> experienceList = Maps.newHashMap();

	public static CrushHandler instance() {
		return base;
	}

	private CrushHandler() {
	}

	public ItemStack getResult(ItemStack stack) {
		for (Entry<ItemStack, ItemStack> entry : crushingList.entrySet()) {
			ItemStack in = entry.getKey();
			if (in.isItemEqual(stack) || (in.getItem() == stack.getItem() && in.getItemDamage() == OreDictionary.WILDCARD_VALUE))
				return entry.getValue();
		}
		return ItemStack.EMPTY;
	}

	public float getExperience(ItemStack stack) {
		for (Entry<ItemStack, Float> entry : experienceList.entrySet()) {
			ItemStack in = entry.getKey();
			if (in.isItemEqual(stack) || (in.getItem() == stack.getItem() && in.getItemDamage() == OreDictionary.WILDCARD_VALUE))
				return entry.getValue();
		}
		return 0f;
	}

	public void addItemStack(ItemStack in, ItemStack out, float exp) {
		if (in.isEmpty() || out.isEmpty())
			return;
		for (ItemStack stack : crushingList.keySet())
			if (in.isItemEqual(stack))
				return;
		this.crushingList.put(in.copy(), out);
		this.experienceList.put(out.copy(), exp);
	}
}