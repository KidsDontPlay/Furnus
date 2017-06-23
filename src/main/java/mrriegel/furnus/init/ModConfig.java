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
		for (Upgrade u : Upgrade.values())
			upgrades.put(u, config.getBoolean(u.name().toLowerCase(), "upgrades", true, "Enable " + u.name() + " upgrade"));

		if (config.hasChanged()) {
			config.save();
		}

	}

}
