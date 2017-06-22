package mrriegel.furnus.gui;

import mrriegel.furnus.tile.TileDevice;
import mrriegel.furnus.tile.TileFurnus;
import mrriegel.furnus.tile.TilePulvus;
import mrriegel.furnus.util.CrushHandler;
import mrriegel.furnus.util.Enums.Upgrade;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.MathHelper;

public class SlotOutput extends Slot {

	private final EntityPlayer player;
	private int removeCount;

	public SlotOutput(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		if (this.getHasStack()) {
			this.removeCount += Math.min(amount, this.getStack().getCount());
		}

		return super.decrStackSize(amount);
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
		this.onCrafting(stack);
		super.onTake(thePlayer, stack);
		return stack;
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount) {
		this.removeCount += amount;
		this.onCrafting(stack);
	}

	@Override
	protected void onCrafting(ItemStack stack) {
		stack.onCrafting(this.player.world, this.player, this.removeCount);
		TileDevice tile = (TileDevice) inventory;
		if (!this.player.world.isRemote) {
			int i = this.removeCount;
			float m = tile instanceof TileFurnus ? FurnaceRecipes.instance().getSmeltingExperience(stack) : tile instanceof TilePulvus ? CrushHandler.instance().getExperience(stack) : 0f;
			float f = m + (m * tile.getAmount(Upgrade.XP)) * 1.5f;

			if (f == 0.0F) {
				i = 0;
			} else if (f < 1.0F) {
				int j = MathHelper.floor(i * f);
				if (j < MathHelper.ceil(i * f) && Math.random() < i * f - j) {
					++j;
				}
				i = j;
			}
			while (i > 0) {
				int k = EntityXPOrb.getXPSplit(i);
				i -= k;
				this.player.world.spawnEntity(new EntityXPOrb(this.player.world, this.player.posX, this.player.posY + 0.5D, this.player.posZ + 0.5D, k));
			}
		}

		this.removeCount = 0;
		if (tile instanceof TileFurnus)
			net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerSmeltedEvent(player, stack);
	}

}
