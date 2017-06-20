package mrriegel.furnus.tile;

import mrriegel.furnus.Furnus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class TileFurnus extends TileDevice {

	@Override
	public boolean openGUI(EntityPlayerMP player) {
		player.openGui(Furnus.instance, 0, world, getX(), getY(), getZ());
		return true;
	}

	@Override
	public ItemStack getResult(ItemStack input) {
		return FurnaceRecipes.instance().getSmeltingResult(input);
	}

}
