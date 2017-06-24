package mrriegel.furnus.init;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import mrriegel.furnus.util.CrushRecipe;
import mrriegel.furnus.util.Enums.Upgrade;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ModConfig {

	public static Configuration config;

	public static List<CrushRecipe> recipes;
	public static String[] blacklistDusts;
	public static boolean dusts;
	public static Map<Upgrade, Boolean> upgrades = Maps.newHashMap();
	public static Map<Upgrade, Integer> maxStacksize = Maps.newHashMap();
	public static double speedMultiplier, speedFuelMultiplier, effiFuelMultiplier;

	@SuppressWarnings("serial")
	public static void refreshConfig(File file) {
		config = new Configuration(file);
		config.load();
		Gson gson = new Gson();
		Property prop = config.get("recipe", "pulvus_recipes", CrushRecipe.getDefaultRecipes().stream().map(r -> gson.toJson(r)).toArray(String[]::new));
		prop.setLanguageKey("pulvus_recipes");
		prop.setComment("Pulvus Recipes" + //
				Configuration.NEW_LINE + "item: modID:itemNname OR oreDictName" + //
				Configuration.NEW_LINE + "amount: #number" + //
				Configuration.NEW_LINE + "metadata: /number");
		recipes = Arrays.stream(prop.getStringList()).map(s -> (CrushRecipe) gson.fromJson(s, new TypeToken<CrushRecipe>() {
		}.getType())).collect(Collectors.toList());
		blacklistDusts = config.getStringList("blacklistDusts", Configuration.CATEGORY_GENERAL, new String[] { "dustCoal" }, "Blacklist for dusts which should not be craftable in pulvus.");
		dusts = config.getBoolean("dusts", "recipe", true, "Enable built in dusts");
		for (Upgrade u : Upgrade.values()) {
			upgrades.put(u, config.getBoolean(u.name().toLowerCase(), "upgrades", true, "Enable " + u.name() + " upgrade"));
			maxStacksize.put(u, u.maxStacksize <= 2 ? u.maxStacksize : config.getInt(u.name().toLowerCase() + "_stacksize", "upgrade_stacksize", u.maxStacksize, 1, 32, "Max stacksize for " + u.name()));
		}
		speedMultiplier = config.getFloat("speedMultiplier", "multiplier", .5f, .05f, 5f, "Multiplier of Speed Upgrade");
		speedFuelMultiplier = config.getFloat("speedFuelMultiplier", "multiplier", .4f, .05f, 5f, "Multiplier of Fuel Consumption of Speed Upgrade");
		effiFuelMultiplier = config.getFloat("effiFuelMultiplier", "multiplier", .3f, .05f, 5f, "Multiplier of Fuel Consumption of Efficiency Upgrade");
		if (config.hasChanged()) {
			config.save();
		}

	}

}
