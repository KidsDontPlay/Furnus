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
	public ItemStack getResult(ItemStack input)
	{
		if( !this.hasCachedResult() )
		{
			this.cachedResult = CrunchHandler.instance().getResult(input);
		}
		return this.cachedResult;
	}

	@Override
	public ItemStack getAntiResult(ItemStack input)
	{
		if( !this.hasCachedAntiResult() )
		{
			this.cachedAntiResult = FurnaceRecipes.instance().getSmeltingResult(input);
		}
		return this.cachedAntiResult;
	}

}
