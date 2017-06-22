package mrriegel.furnus.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

	public static void init() {
		GameRegistry.addSmelting(new ItemStack(ModItems.dust, 1, 0), new ItemStack(Items.IRON_INGOT), .1f);
		GameRegistry.addSmelting(new ItemStack(ModItems.dust, 1, 1), new ItemStack(Items.GOLD_INGOT), .1f);
	}
}
