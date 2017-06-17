package mrriegel.furnus.init;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import mrriegel.furnus.util.Recipe;
import mrriegel.limelib.util.Utils;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ModConfig {

	public static Configuration config;

	public static List<Recipe> recipes;

	public static void refreshConfig(File file) {
		config = new Configuration(file);
		config.load();

		//		Property prop = config.get(Configuration.CATEGORY_GENERAL, "pulvus_recipes", Utils.getGSON().toJson(Recipe.getDefaultRecipes()));
		//		prop.setLanguageKey("pulvus_recipes");
		//		prop.setValidationPattern(null);
		//		prop.setComment("Pulvus Recipes");
		//		String rlist = prop.getString();
		//		recipes = Utils.getGSON().fromJson(rlist, new TypeToken<List<Recipe>>() {
		//		}.getType());
		Gson gson = new Gson();
		Property prop = config.get(Configuration.CATEGORY_GENERAL, "pulvus_recipes", Recipe.getDefaultRecipes().stream().map(r->gson.toJson(r)).toArray(String[]::new));
		prop.setLanguageKey("pulvus_recipes");
		prop.setValidationPattern(null);
		prop.setComment("Pulvus Recipes");
		String[] rlist = prop.getStringList();
		recipes = Lists.newArrayList();
		for (String rec : rlist)
			recipes.add(gson.fromJson(rec, new TypeToken<Recipe>() {
			}.getType()));

		if (config.hasChanged()) {
			config.save();
		}

	}

}
