package mrriegel.furnus.item;

import java.util.List;

import com.google.common.collect.Iterables;

import mrriegel.furnus.init.ModConfig;
import mrriegel.furnus.util.CreativeTab;
import mrriegel.furnus.util.Enums.Upgrade;
import mrriegel.limelib.item.CommonSubtypeItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemUpgrade extends CommonSubtypeItem {

	public ItemUpgrade() {
		super("upgrade", Upgrade.values().length);
		setCreativeTab(CreativeTab.tab);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		if (stack.getItemDamage() >= Upgrade.values().length)
			return 1;
		return ModConfig.maxStacksize.get(Upgrade.values()[stack.getItemDamage()]);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(I18n.format("item.furnus:upgrade_" + stack.getItemDamage() + ".tip"));
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		super.getSubItems(tab, subItems);
		if ("".isEmpty())
			Iterables.removeIf(subItems, s -> s.getItem() == this && !ModConfig.upgrades.get(Upgrade.values()[s.getItemDamage()]));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		if (stack.getItemDamage() < Upgrade.values().length && !ModConfig.upgrades.get(Upgrade.values()[stack.getItemDamage()]))
			stack.setCount(0);
		return super.initCapabilities(stack, nbt);
	}

}
