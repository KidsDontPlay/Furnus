package mrriegel.furnus.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import mrriegel.furnus.init.ModConfig;
import mrriegel.limelib.gui.ContainerNull;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

public class CrushRecipe {
	String inputItem, outputItem;
	float experience;

	public CrushRecipe(String in, String out, float exp) {
		super();
		this.inputItem = in;
		this.outputItem = out;
		this.experience = exp;
	}

	public void register() {
		List<ItemStack> inl = string2Stacks(inputItem);
		List<ItemStack> outl = string2Stacks(outputItem);
		for (ItemStack in : inl)
			for (ItemStack out : outl)
				CrushHandler.instance().addItemStack(in, out, experience);
	}

	private static List<ItemStack> string2Stacks(String s) {
		int size = parse(s, '#', 1), meta = parse(s, '/', 0);
		int first = s.length();
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == '#' || s.charAt(i) == '/') {
				first = i;
				break;
			}
		String itemName = s.substring(0, first);
		Item item = null;
		if (StringUtils.countMatches(itemName, ":") == 1 && (item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))) != null)
			return Collections.singletonList(new ItemStack(item, size, meta));
		else if (StringUtils.countMatches(s, ":") == 0)
			return OreDictionary.getOres(itemName).stream().map(stack -> ItemHandlerHelper.copyStackWithSize(stack, size)).collect(Collectors.toList());
		else
			throw new RuntimeException("Invalid pulvus Recipes: " + s);
	}

	private static int parse(String s, char c, int defaultVal) {
		int in = s.indexOf(c);
		if (in != -1) {
			for (int i = Math.min(in + 4, s.length()); i >= in; i--) {
				String k = s.substring(in + 1, i);
				try {
					return Integer.parseInt(k);
				} catch (NumberFormatException e) {
				}
			}
		}
		return defaultVal;
	}

	public static List<CrushRecipe> getDefaultRecipes() {
		List<CrushRecipe> lis = Lists.newArrayList();
		lis.add(new CrushRecipe("cobblestone", "gravel", .1f));
		lis.add(new CrushRecipe("gravel", "sand", .1f));
		lis.add(new CrushRecipe("stone", "cobblestone", .1f));
		lis.add(new CrushRecipe("minecraft:stonebrick/-1", "cobblestone", .1f));
		lis.add(new CrushRecipe("oreQuartz", "gemQuartz#3", .3f));
		lis.add(new CrushRecipe("oreCoal", "minecraft:coal#3", .3f));
		lis.add(new CrushRecipe("oreLapis", "gemLapis#3", .3f));
		lis.add(new CrushRecipe("oreRedstone", "dustRedstone#8", .3f));
		lis.add(new CrushRecipe("minecraft:quartz_block/-1", "gemQuartz#4", .1f));
		lis.add(new CrushRecipe("glowstone", "dustGlowstone#4", .1f));
		lis.add(new CrushRecipe("minecraft:blaze_rod", "minecraft:blaze_powder#4", .4f));
		lis.add(new CrushRecipe("minecraft:bone", "minecraft:dye/15#6", .1f));
		lis.add(new CrushRecipe("minecraft:wool/-1", "minecraft:string/0#4", .1f));
		lis.add(new CrushRecipe("sugarcane", "minecraft:sugar#3", .2f));
		return lis;
	}

	public static void registerDefaultOreRecipes() {
		for (String ore : OreDictionary.getOreNames()) {
			add(ore, "ore", "dust", 2, .5f);
			add(ore, "ore", "gem", 3, .5f);
			add(ore, "ingot", "dust", 1, .1f);
		}
		for (Item item : ForgeRegistries.ITEMS) {
			if (item.getRegistryName().getResourcePath().contains("flower")) {
				for (int i = 0; i < 16; i++) {
					ItemStack s = new ItemStack(item, 1, i);
					InventoryCrafting ic = new InventoryCrafting(new ContainerNull(), 3, 3);
					ic.setInventorySlotContents(0, s);
					ItemStack result = ItemStack.EMPTY;
					try {
						result = CraftingManager.findMatchingResult(ic, null).copy();
					} catch (Exception e) {
					}
					if (!result.isEmpty() && result.getCount() == 1) {
						CrushHandler.instance().addItemStack(s, ItemHandlerHelper.copyStackWithSize(result, 3), .1f);
					}
					if (!item.getHasSubtypes())
						break;
				}
			}
		}
	}

	private static void add(String ore, String in, String out, int amount, float exp) {
		List<String> blacks = Lists.newArrayList(ModConfig.blacklistDusts);
		if (ore.length() <= in.length())
			return;
		String mat = ore.substring(in.length());
		List<ItemStack> outs = OreDictionary.getOres(out + mat);
		if (ore.startsWith(in) && !outs.isEmpty() && !blacks.contains(out + mat))
			for (ItemStack stack : OreDictionary.getOres(ore))
				CrushHandler.instance().addItemStack(stack, ItemHandlerHelper.copyStackWithSize(outs.get(0), amount), exp);
	}
}
