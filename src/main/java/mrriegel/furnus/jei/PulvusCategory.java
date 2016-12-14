package mrriegel.furnus.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.plugins.vanilla.furnace.FurnaceRecipeCategory;
import mrriegel.furnus.Furnus;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class PulvusCategory extends FurnaceRecipeCategory<PulvusWrapper> {
	private final IDrawable background;

	public PulvusCategory(IGuiHelper guiHelper) {
		super(guiHelper);
		ResourceLocation location = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");
		background = guiHelper.createDrawable(location, 55, 16, 82, 54);
	}

	@Override
	public String getUid() {
		return Furnus.MODID + ".pulvus";
	}

	@Override
	public String getTitle() {
		return "Pulvus";
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
	}

	@Override
	public void drawAnimations(Minecraft minecraft) {
		flame.draw(minecraft, 2, 20);
		arrow.draw(minecraft, 24, 18);
		minecraft.fontRendererObj.drawString("grind", 24, 36, 0x404040);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, PulvusWrapper recipeWrapper) {
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, PulvusWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		itemStacks.init(inputSlot, true, 0, 0);
		itemStacks.init(outputSlot, false, 60, 18);
		itemStacks.setFromRecipe(inputSlot, recipeWrapper.getInputs());
		itemStacks.setFromRecipe(outputSlot, recipeWrapper.getOutputs());

	}

}
