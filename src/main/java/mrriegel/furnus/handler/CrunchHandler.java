package mrriegel.furnus.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CrunchHandler {
	private static final CrunchHandler base = new CrunchHandler();
	private Map<ItemStack, ItemStack> crushingList = new HashMap<ItemStack, ItemStack>();
	private Map<ItemStack, Float> experienceList = new HashMap<ItemStack, Float>();

	public static CrunchHandler instance() {
		return base;
	}

	private CrunchHandler() {
		addItemStack(new ItemStack(Blocks.cobblestone), new ItemStack(Blocks.gravel), 0.1F);
		addItemStack(new ItemStack(Blocks.gravel), new ItemStack(Blocks.sand), 0.1F);
		addItemStack(new ItemStack(Blocks.stone, 1, 0), new ItemStack(Blocks.cobblestone), 0.1F);
		addItemStack(new ItemStack(Blocks.stonebrick), new ItemStack(Blocks.cobblestone), 0.1F);
		addItemStack(new ItemStack(Blocks.quartz_ore), new ItemStack(Items.quartz, 3), 0.3F);
		addItemStack(new ItemStack(Blocks.coal_ore), new ItemStack(Items.coal, 3), 0.3F);
		addItemStack(new ItemStack(Blocks.lapis_ore), new ItemStack(Items.dye, 8, 4), 0.3F);
		addItemStack(new ItemStack(Blocks.diamond_ore), new ItemStack(Items.diamond, 2), 1.0F);
		if (!Arrays.asList(ConfigurationHandler.blacklistDusts).contains("dustRedstone"))
			addItemStack(new ItemStack(Blocks.redstone_ore), new ItemStack(Items.redstone, 8), 0.3F);
		addItemStack(new ItemStack(Blocks.emerald_ore), new ItemStack(Items.emerald, 2), 1.0F);
		addBlock(Blocks.quartz_block, new ItemStack(Items.quartz, 4), 0.3F);
		if (!Arrays.asList(ConfigurationHandler.blacklistDusts).contains("dustGlowstone"))
			addItemStack(new ItemStack(Blocks.glowstone), new ItemStack(Items.glowstone_dust, 4),
					0.3F);
		addItemStack(new ItemStack(Items.blaze_rod), new ItemStack(Items.blaze_powder, 4), 0.4F);
		addItemStack(new ItemStack(Items.bone), new ItemStack(Items.dye, 6, 15), 0.1F);
		addBlock(Blocks.wool, new ItemStack(Items.string, 4), 0.1F);
	}

	public static ItemStack resize(ItemStack stack, int size) {
		if (stack == null)
			return null;
		stack.stackSize = size;
		return stack.copy();
	}

	public ItemStack getResult(ItemStack stack) {
		for (Entry<ItemStack, ItemStack> entry : crushingList.entrySet()) {
			ItemStack in = entry.getKey().copy();
			if (in.isItemEqual(stack)
					|| (in.getItem() == stack.getItem() && in.getItemDamage() == OreDictionary.WILDCARD_VALUE))
				return entry.getValue().copy();
		}
		return null;
	}

	public float getExperience(ItemStack stack) {
		for (Entry<ItemStack, Float> entry : experienceList.entrySet()) {
			ItemStack in = entry.getKey().copy();
			if (in.isItemEqual(stack)
					|| (in.getItem() == stack.getItem() && in.getItemDamage() == OreDictionary.WILDCARD_VALUE))
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

	public void addItemStack(ItemStack p_151394_1_, ItemStack p_151394_2_, float p_151394_3_) {
		if (p_151394_1_ == null || p_151394_2_ == null)
			return;
		this.crushingList.put(p_151394_1_.copy(), p_151394_2_);
		this.experienceList.put(p_151394_2_, Float.valueOf(p_151394_3_));
	}
}