package mrriegel.furnus.item;

import java.util.List;

import mrriegel.furnus.util.CreativeTab;
import mrriegel.furnus.util.Enums.Upgrade;
import mrriegel.limelib.item.CommonSubtypeItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemUpgrade extends CommonSubtypeItem {

	public ItemUpgrade() {
		super("upgrade", Upgrade.values().length);
		setCreativeTab(CreativeTab.tab);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return Upgrade.values()[stack.getItemDamage()].maxStacksize;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(I18n.format("item.furnus:upgrade_" + stack.getItemDamage() + ".tip"));
	}

}
