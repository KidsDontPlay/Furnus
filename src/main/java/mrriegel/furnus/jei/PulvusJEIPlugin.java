package mrriegel.furnus.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class PulvusJEIPlugin implements IModPlugin {

	@Override
	public void register(IModRegistry registry) {
		registry.addRecipeCategories(new PulvusCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeHandlers(new PulvusHandler());
		registry.addRecipes(PulvusHandler.getRecipes());
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime arg0) {
	}

}
