package mrriegel.furnus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import mrriegel.furnus.block.ModBlocks;
import mrriegel.furnus.handler.ConfigHandler;
import mrriegel.furnus.handler.CrunchHandler;
import mrriegel.furnus.handler.GuiHandler;
import mrriegel.furnus.item.ModItems;
import mrriegel.limelib.LimeLib;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Mod(modid = Furnus.MODID, name = Furnus.MODNAME, version = Furnus.VERSION, dependencies = "required-after:limelib@[1.5.1,)")
public class Furnus {
	public static final String MODID = "furnus";
	public static final String VERSION = "1.92";
	public static final String MODNAME = "Furnus";

	@Instance(Furnus.MODID)
	public static Furnus instance;
	private List<Recipe> recipes;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		File configFile = event.getSuggestedConfigurationFile();
		ConfigHandler.config = new Configuration(configFile);
		ConfigHandler.config.load();
		ConfigHandler.refreshConfig();
		File questFile = new File(event.getModConfigurationDirectory(), "pulvus_recipes.json");
		if (!questFile.exists()) {
			questFile.createNewFile();
			FileWriter fw = new FileWriter(questFile);
			List<Recipe> lis = Lists.newArrayList();
			lis.add(new Recipe("cobblestone:1", "minecraft:gravel:0:1", .1F));
			lis.add(new Recipe("minecraft:gravel:0:1", "minecraft:sand:0:1", .1F));
			lis.add(new Recipe("stone:1", "cobblestone:1", .1F));
			lis.add(new Recipe("minecraft:stonebrick:-1:1", "cobblestone:1", .1F));
			lis.add(new Recipe("oreQuartz:1", "gemQuartz:3", .3F));
			lis.add(new Recipe("denseoreQuartz:1", "gemQuartz:9", .9F));
			lis.add(new Recipe("oreCoal:1", "minecraft:coal:0:3", .3F));
			lis.add(new Recipe("oreNetherCoal:1", "minecraft:coal:0:6", .6F));
			lis.add(new Recipe("denseoreCoal:1", "minecraft:coal:0:9", .9F));
			lis.add(new Recipe("oreLapis:1", "gemLapis:8", .3F));
			lis.add(new Recipe("oreNetherLapis:1", "gemLapis:16", .6F));
			lis.add(new Recipe("denseoreLapis:1", "gemLapis:24", .9F));
			lis.add(new Recipe("denseoreDiamond:1", "gemDiamond:6", 3F));
			lis.add(new Recipe("oreRedstone:1", "dustRedstone:8", .3F));
			lis.add(new Recipe("oreNetherRedstone:1", "dustRedstone:16", .6F));
			lis.add(new Recipe("denseoreRedstone:1", "dustRedstone:24", .9F));
			lis.add(new Recipe("denseoreEmerald:1", "gemEmerald:6", 3F));
			lis.add(new Recipe("minecraft:quartz_block:-1:1", "gemQuartz:4", .3F));
			lis.add(new Recipe("glowstone:1", "dustGlowstone:4", .3F));
			lis.add(new Recipe("minecraft:blaze_rod:0:1", "minecraft:blaze_powder:0:4", .4F));
			lis.add(new Recipe("minecraft:bone:0:1", "minecraft:dye:15:6", .1F));
			lis.add(new Recipe("minecraft:wool:-1:1", "minecraft:string:0:4", .1F));
			lis.add(new Recipe("minecraft:red_flower:0:1", "minecraft:dye:1:3", .1F));
			lis.add(new Recipe("minecraft:red_flower:4:1", "minecraft:dye:1:3", .1F));
			lis.add(new Recipe("minecraft:red_flower:3:1", "minecraft:dye:7:3", .1F));
			lis.add(new Recipe("minecraft:red_flower:6:1", "minecraft:dye:7:3", .1F));
			lis.add(new Recipe("minecraft:red_flower:8:1", "minecraft:dye:7:3", .1F));
			lis.add(new Recipe("minecraft:red_flower:7:1", "minecraft:dye:9:3", .1F));
			lis.add(new Recipe("minecraft:yellow_flower:0:1", "minecraft:dye:11:3", .1F));
			lis.add(new Recipe("minecraft:red_flower:1:1", "minecraft:dye:12:3", .1F));
			lis.add(new Recipe("minecraft:red_flower:2:1", "minecraft:dye:13:3", .1F));
			lis.add(new Recipe("minecraft:red_flower:5:1", "minecraft:dye:14:3", .1F));
			fw.write(new GsonBuilder().setPrettyPrinting().create().toJson(lis));
			fw.close();
		}
		recipes = new Gson().fromJson(new BufferedReader(new FileReader(questFile)), new TypeToken<List<Recipe>>() {
		}.getType());
		ModBlocks.init();
		ModItems.init();
		CraftingRecipes.init();
		if (event.getSide().isClient()) {
			initModels();
		}
	}

	private void initModels() {
		ModBlocks.furnus.initModel();
		ModBlocks.pulvus.initModel();
		ModItems.upgrade.initModel();
		ModItems.dust.initModel();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		for (Recipe r : recipes) {
			List<ItemStack> inl = Lists.newArrayList();
			List<ItemStack> outl = Lists.newArrayList();
			if (!string2Stack(r.inputItem).isEmpty())
				inl.add(string2Stack(r.inputItem));
			else
				inl.addAll(oreName2Stacklist(r.inputItem));
			if (!string2Stack(r.outputItem).isEmpty())
				outl.add(string2Stack(r.outputItem));
			else {
				if (!oreName2Stacklist(r.outputItem).isEmpty())
					outl.add(oreName2Stacklist(r.outputItem).get(0));
			}
			for (ItemStack in : inl)
				for (ItemStack out : outl)
					CrunchHandler.instance().addItemStack(in, out, r.experience);
		}
		List<String> black = Lists.newArrayList(ConfigHandler.blacklistDusts);
		for (String ore : OreDictionary.getOreNames()) {
			if (ore.startsWith("ore") && !OreDictionary.getOres("dust" + ore.substring(3)).isEmpty() && !black.contains("dust" + ore.substring(3)) && !OreDictionary.getOres(ore).isEmpty())
				for (ItemStack stack : OreDictionary.getOres(ore))
					CrunchHandler.instance().addItemStack(stack, ItemHandlerHelper.copyStackWithSize(OreDictionary.getOres("dust" + ore.substring(3)).get(0), 2), 0.5F);
			else if (ore.startsWith("ore") && !OreDictionary.getOres("gem" + ore.substring(3)).isEmpty() && !black.contains("gem" + ore.substring(3)) && !OreDictionary.getOres(ore).isEmpty())
				for (ItemStack stack : OreDictionary.getOres(ore))
					CrunchHandler.instance().addItemStack(stack, ItemHandlerHelper.copyStackWithSize(OreDictionary.getOres("gem" + ore.substring(3)).get(0), 2), 0.4F);
			else if (ore.startsWith("oreNether") && !OreDictionary.getOres("dust" + ore.substring(9)).isEmpty() && !black.contains("dust" + ore.substring(9)) && !OreDictionary.getOres(ore).isEmpty())
				for (ItemStack stack : OreDictionary.getOres(ore))
					CrunchHandler.instance().addItemStack(stack, ItemHandlerHelper.copyStackWithSize(OreDictionary.getOres("dust" + ore.substring(9)).get(0), 4), 1F);
			else if (ore.startsWith("oreNether") && !OreDictionary.getOres("gem" + ore.substring(9)).isEmpty() && !black.contains("gem" + ore.substring(9)) && !OreDictionary.getOres(ore).isEmpty())
				for (ItemStack stack : OreDictionary.getOres(ore))
					CrunchHandler.instance().addItemStack(stack, ItemHandlerHelper.copyStackWithSize(OreDictionary.getOres("gem" + ore.substring(9)).get(0), 4), 1.2F);
			else if (ore.startsWith("denseore") && !OreDictionary.getOres("dust" + ore.substring(8)).isEmpty() && !black.contains("dust" + ore.substring(8)) && !OreDictionary.getOres(ore).isEmpty())
				for (ItemStack stack : OreDictionary.getOres(ore))
					CrunchHandler.instance().addItemStack(stack, ItemHandlerHelper.copyStackWithSize(OreDictionary.getOres("dust" + ore.substring(8)).get(0), 6), 2F);
			else if (ore.startsWith("denseore") && !OreDictionary.getOres("gem" + ore.substring(8)).isEmpty() && !black.contains("gem" + ore.substring(8)) && !OreDictionary.getOres(ore).isEmpty())
				for (ItemStack stack : OreDictionary.getOres(ore))
					CrunchHandler.instance().addItemStack(stack, ItemHandlerHelper.copyStackWithSize(OreDictionary.getOres("gem" + ore.substring(8)).get(0), 6), 2F);
			else if (ore.startsWith("ingot") && !OreDictionary.getOres("dust" + ore.substring(5)).isEmpty() && !black.contains("dust" + ore.substring(5)) && !OreDictionary.getOres(ore).isEmpty())
				for (ItemStack stack : OreDictionary.getOres(ore))
					CrunchHandler.instance().addItemStack(stack, OreDictionary.getOres("dust" + ore.substring(5)).get(0), 0.1F);
		}

	}

	private ItemStack string2Stack(String s) {
		ItemStack stack = ItemStack.EMPTY;
		if (StringUtils.countMatches(s, ":") == 3) {
			Item tmp = Item.REGISTRY.getObject(new ResourceLocation(s.split(":")[0], s.split(":")[1]));
			if (tmp != null) {
				stack = new ItemStack(tmp, Integer.valueOf(s.split(":")[3]), Integer.valueOf(s.split(":")[2]) == -1 ? OreDictionary.WILDCARD_VALUE : Integer.valueOf(s.split(":")[2]));
			} else
				LimeLib.log.warn("Couldn't find item for " + s);
		}
		return stack;
	}

	private List<ItemStack> oreName2Stacklist(String s) {
		List<ItemStack> lis = Lists.newArrayList();
		if (StringUtils.countMatches(s, ":") == 1) {
			for (ItemStack ore : OreDictionary.getOres(s.split(":")[0])) {
				ItemStack stack = ore.copy();
				stack.setCount(Integer.valueOf(s.split(":")[1]));
				lis.add(stack);
			}
		}
		return lis;
	}

	private class Recipe {
		String inputItem, outputItem;
		float experience;

		public Recipe(String in, String out, float exp) {
			super();
			this.inputItem = in;
			this.outputItem = out;
			this.experience = exp;
		}
	}

}
