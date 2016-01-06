package mrriegel.furnus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import mrriegel.furnus.block.ModBlocks;
import mrriegel.furnus.handler.ConfigurationHandler;
import mrriegel.furnus.handler.CrunchHandler;
import mrriegel.furnus.handler.GuiHandler;
import mrriegel.furnus.handler.PacketHandler;
import mrriegel.furnus.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Furnus.MODID, name = Furnus.MODNAME, version = Furnus.VERSION)
public class Furnus {
	public static final String MODID = "furnus";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "Furnus";

	@Instance(Furnus.MODID)
	public static Furnus instance;
	private Map<String, String> recipes;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		File configFile = event.getSuggestedConfigurationFile();
		ConfigurationHandler.config = new Configuration(configFile);
		ConfigurationHandler.config.load();
		ConfigurationHandler.refreshConfig();
		PacketHandler.init();
		File questFile = new File(event.getModConfigurationDirectory(), "furnus_recipes.json");
		if (!questFile.exists()) {
			questFile.createNewFile();
			FileWriter fw = new FileWriter(questFile);
			Map<String, String> map = new TreeMap<String, String>();
			map.put("cobblestone:1", "minecraft:gravel:0:1");
			map.put("minecraft:gravel:0:1", "minecraft:sand:0:1");
			map.put("stone:1", "cobblestone:1");
			map.put("minecraft:stonebrick:-1:1", "cobblestone:1");
			map.put("oreQuartz:1", "gemQuartz:3");
			map.put("oreCoal:1", "minecraft:coal:0:3");
			map.put("oreLapis:1", "gemLapis:8");
			map.put("oreDiamond:1", "gemDiamond:2");
			map.put("oreRedstone:1", "dustRedstone:8");
			map.put("oreEmerald:1", "gemEmerald:2");
			map.put("minecraft:quartz_block:-1:1", "gemQuartz:4");
			map.put("glowstone:1", "dustGlowstone:4");
			map.put("minecraft:blaze_rod:0:1", "minecraft:blaze_powder:0:4");
			map.put("minecraft:bone:0:1", "minecraft:dye:15:6");
			map.put("minecraft:wool:-1:1", "minecraft:string:0:4");
			fw.write(new GsonBuilder().setPrettyPrinting().create().toJson(map));
			fw.close();
		}
		recipes = new Gson().fromJson(new BufferedReader(new FileReader(questFile)),
				new TypeToken<Map<String, String>>() {
				}.getType());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		ModBlocks.init();
		ModItems.init();
		CraftingRecipes.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		for (Entry<String, String> e : recipes.entrySet()) {
			List<ItemStack> inl = new ArrayList<ItemStack>();
			List<ItemStack> outl = new ArrayList<ItemStack>();
			if (string2Stack(e.getKey()) != null)
				inl.add(string2Stack(e.getKey()));
			else
				inl.addAll(string2Stacklist(e.getKey()));
			if (string2Stack(e.getValue()) != null)
				outl.add(string2Stack(e.getValue()));
			else {
				if (!string2Stacklist(e.getValue()).isEmpty())
					outl.add(string2Stacklist(e.getValue()).get(0));
			}
			for (ItemStack in : inl)
				for (ItemStack out : outl)
					CrunchHandler.instance().addItemStack(in, out, 0.1f);

		}
		List<String> black = Arrays.asList(ConfigurationHandler.blacklistDusts);
		for (String ore : OreDictionary.getOreNames()) {
			if (ore.startsWith("ore")
					&& !OreDictionary.getOres("dust" + ore.substring(3)).isEmpty()
					&& !black.contains("dust" + ore.substring(3)))
				CrunchHandler.instance().addItemStack(
						OreDictionary.getOres(ore).get(0),
						CrunchHandler.resize(OreDictionary.getOres("dust" + ore.substring(3))
								.get(0), 2), 0.4F);
			else if (ore.startsWith("ingot")
					&& !OreDictionary.getOres("dust" + ore.substring(5)).isEmpty()
					&& !black.contains("dust" + ore.substring(5)))
				CrunchHandler.instance().addItemStack(OreDictionary.getOres(ore).get(0),
						OreDictionary.getOres("dust" + ore.substring(5)).get(0), 0.1F);
		}

	}

	private ItemStack string2Stack(String s) {
		ItemStack stack = null;
		if (StringUtils.countMatches(s, ":") == 3) {
			ItemStack tmp = GameRegistry.findItemStack(s.split(":")[0], s.split(":")[1],
					Integer.valueOf(s.split(":")[3]));
			if (tmp != null) {
				stack = tmp.copy();
				stack.setItemDamage(Integer.valueOf(s.split(":")[2]) == -1 ? OreDictionary.WILDCARD_VALUE
						: Integer.valueOf(s.split(":")[2]));
			}
		}
		if (stack == null)
			return null;

		return stack.copy();
	}

	private List<ItemStack> string2Stacklist(String s) {
		List<ItemStack> lis = new ArrayList<ItemStack>();
		if (StringUtils.countMatches(s, ":") == 1) {
			if (OreDictionary.doesOreNameExist(s.split(":")[0])) {
				for (ItemStack ore : OreDictionary.getOres(s.split(":")[0])) {
					ItemStack stack = ore.copy();
					stack.stackSize = Integer.valueOf(s.split(":")[1]);
					lis.add(stack);
				}
			}
		}
		return lis;
	}
}
