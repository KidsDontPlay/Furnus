package mrriegel.furnus.jei;

import java.util.Collections;
import java.util.List;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class PulvusWrapper extends BlankRecipeWrapper {

	private ItemStack input, output;

	public PulvusWrapper(ItemStack input, ItemStack output) {
		super();
		this.input = input;
		this.output = output;
	}

	@Override
	public List getInputs() {
		return Collections.singletonList(input);
	}

	@Override
	public List getOutputs() {
		return Collections.singletonList(output);
	}

}
