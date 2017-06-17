package mrriegel.furnus.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

public class Recipe {
	String inputItem, outputItem;
	float experience;

	public Recipe(String in, String out, float exp) {
		super();
		this.inputItem = in;
		this.outputItem = out;
		this.experience = exp;
	}

	public void register() {
		//TODO
		if ("".isEmpty())
			return;
		List<ItemStack> inl = Lists.newArrayList();
		List<ItemStack> outl = Lists.newArrayList();
		if (!string2Stack(inputItem).isEmpty())
			inl.add(string2Stack(inputItem));
		else
			inl.addAll(string2Stacks(inputItem));
		if (!string2Stack(outputItem).isEmpty())
			outl.add(string2Stack(outputItem));
		else {
			if (!string2Stacks(outputItem).isEmpty())
				outl.add(string2Stacks(outputItem).get(0));
		}
		for (ItemStack in : inl)
			for (ItemStack out : outl)
				CrushHandler.instance().addItemStack(in, out, experience);
	}

	private static ItemStack string2Stack(String s) {
		ItemStack stack = string2Stacks(s).stream().findFirst().orElse(ItemStack.EMPTY);
		return stack;
	}

	private static List<ItemStack> string2Stacks(String s) {
		int size = parse(s, '#', 1), meta = parse(s, '/', 0);
		int first = s.length() ;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == '#' || s.charAt(i) == '/') {
				first = i;
				break;
			}
		//		int hashtag=s.contains("#")?s.indexOf('#'):1000,slash=s.contains("/")?s.indexOf('/'):1000;
		//		first=s.indexOf
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
			for (int i = Math.min(in + 4, s.length() - 1); i >= in + 1; i--) {
				String k = s.substring(in + 1, i);
				try {
					return Integer.parseInt(k);
				} catch (NumberFormatException e) {
				}
			}
		}
		return defaultVal;
	}

	public static List<Recipe> getDefaultRecipes() {
		List<Recipe> lis = Lists.newArrayList();
		lis.add(new Recipe("cobblestone", "gravel", .1f));
		lis.add(new Recipe("gravel", "sand", .1f));
		lis.add(new Recipe("stone", "cobblestone", .1f));
		lis.add(new Recipe("minecraft:stonebrick/-1", "cobblestone", .1f));
		lis.add(new Recipe("oreQuartz", "gemQuartz#3", .3f));
		lis.add(new Recipe("oreCoal", "minecraft:coal#3", .3f));
		lis.add(new Recipe("oreLapis", "gemLapis#3", .3f));
		lis.add(new Recipe("oreRedstone", "dustRedstone#8", .3f));
		lis.add(new Recipe("minecraft:quartz_block/-1", "gemQuartz#4", .1f));
		lis.add(new Recipe("glowstone", "dustGlowstone#4", .1f));
		lis.add(new Recipe("minecraft:blaze_rod", "minecraft:blaze_powder#4", .4f));
		lis.add(new Recipe("minecraft:bone", "minecraft:dye/15#6", .1f));
		lis.add(new Recipe("minecraft:wool/-1", "minecraft:string/0#4", .1f));
		lis.add(new Recipe("minecraft:red_flower", "minecraft:dye/1#3", .1f));
		lis.add(new Recipe("minecraft:red_flower/1", "minecraft:dye/12#3", .1f));
		lis.add(new Recipe("minecraft:red_flower/2", "minecraft:dye/13#3", .1f));
		lis.add(new Recipe("minecraft:red_flower/3", "minecraft:dye/7#3", .1f));
		lis.add(new Recipe("minecraft:red_flower/4", "minecraft:dye/1#3", .1f));
		lis.add(new Recipe("minecraft:red_flower/5", "minecraft:dye/14#3", .1f));
		lis.add(new Recipe("minecraft:red_flower/6", "minecraft:dye/7#3", .1f));
		lis.add(new Recipe("minecraft:red_flower/7", "minecraft:dye/9#3", .1f));
		lis.add(new Recipe("minecraft:red_flower/8", "minecraft:dye/7#3", .1f));
		lis.add(new Recipe("minecraft:yellow_flower", "minecraft:dye/11#3", .1f));
		return lis;
	}
}
