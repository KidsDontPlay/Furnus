package mrriegel.furnus.block;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.handler.CrunchHandler;
import mrriegel.furnus.handler.GuiHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class TilePulvus extends AbstractMachine {

	@Override
	public boolean openGUI(EntityPlayerMP player) {
		player.openGui(Furnus.instance, GuiHandler.PULVUS, world, getX(), getY(), getZ());
		return true;
	}

	@Override
	public ItemStack getResult(ItemStack input) {
		return CrunchHandler.instance().getResult(input);
	}

	@Override
	public ItemStack getAntiResult(ItemStack input) {
		return FurnaceRecipes.instance().getSmeltingResult(input);
	}

}
