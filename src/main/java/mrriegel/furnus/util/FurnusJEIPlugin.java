package mrriegel.furnus.util;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.gui.elements.DrawableBlank;
import mezz.jei.ingredients.Ingredients;
import mezz.jei.plugins.vanilla.furnace.SmeltingRecipe;
import mrriegel.furnus.Furnus;
import mrriegel.furnus.gui.GuiDevice;
import mrriegel.furnus.init.ModBlocks;
import mrriegel.limelib.gui.GuiDrawer;
import mrriegel.limelib.gui.GuiDrawer.Direction;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class FurnusJEIPlugin implements IModPlugin {

	@Override
	public void register(IModRegistry registry) {
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.furnus), VanillaRecipeCategoryUid.SMELTING);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.pulvus), Furnus.MODID + ".pulvus");
		registry.handleRecipes(Wrapper.class, r -> r, Furnus.MODID + ".pulvus");
		List<Wrapper> lis = CrushHandler.instance().crushingList.entrySet().stream().map(e -> new Wrapper(e.getKey(), e.getValue())).collect(Collectors.toList());
		lis.sort((o1, o2) -> {
			Ingredients ing1 = new Ingredients();
			o1.getIngredients(ing1);
			Ingredients ing2 = new Ingredients();
			o2.getIngredients(ing2);
			ing1.getOutputs(ItemStack.class);
			Integer id1 = Item.getIdFromItem(ing1.getOutputs(ItemStack.class).get(0).get(0).getItem());
			Integer id2 = Item.getIdFromItem(ing2.getOutputs(ItemStack.class).get(0).get(0).getItem());
			if (id1.compareTo(id2) == 0)
				return new Integer(ing1.getOutputs(ItemStack.class).get(0).get(0).getItemDamage()).compareTo(ing2.getOutputs(ItemStack.class).get(0).get(0).getItemDamage());
			return id1.compareTo(id2);
		});
		registry.addRecipes(lis, Furnus.MODID + ".pulvus");
		registry.addRecipeClickArea(GuiDevice.Furnus.class, 45, 20, 30, 60, VanillaRecipeCategoryUid.SMELTING);
		registry.addRecipeClickArea(GuiDevice.Pulvus.class, 45, 20, 30, 60, Furnus.MODID + ".pulvus");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new Category());
	}

	private static class Category implements IRecipeCategory<Wrapper> {

		@Override
		public String getUid() {
			return Furnus.MODID + ".pulvus";
		}

		@Override
		public String getTitle() {
			return "Pulvus";
		}

		@Override
		public String getModName() {
			return Furnus.MODNAME;
		}

		@Override
		public IDrawable getBackground() {
			return new DrawableBlank(100, 20);
		}

		@Override
		public void drawExtras(Minecraft minecraft) {
			GuiDrawer drawer = new GuiDrawer(0, 0, 0, 0, 0);
			drawer.drawColoredRectangle(19, 0, 70, 20, 0x66FFD39B);
			drawer.drawFrame(19, 0, 69, 19, 1, 0x668B4513);
			drawer.drawProgressArrow(43, 2, (minecraft.world.getTotalWorldTime() % 100) / 100f, Direction.RIGHT);
			drawer.drawSlot(20, 1);
			drawer.drawSlot(70, 1);
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients) {
			IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
			guiItemStacks.init(0, true, 20, 1);
			guiItemStacks.init(1, false, 70, 1);
			guiItemStacks.set(ingredients);
		}

	}

	private static class Wrapper extends SmeltingRecipe {

		ItemStack output;

		public Wrapper(ItemStack input, ItemStack output) {
			super(Lists.newArrayList(input), output);
			this.output = output;
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			float experience = CrushHandler.instance().getExperience(output);
			minecraft.fontRenderer.drawString(experience + " XP", -12, 5, Color.gray.getRGB());
		}

	}

}
