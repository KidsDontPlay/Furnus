package mrriegel.furnus.block;

import java.util.Random;

import mrriegel.furnus.CreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AbstractBlock extends BlockContainer {
	@SideOnly(Side.CLIENT)
	protected IIcon top, side, front, front_lit, bottom;

	public AbstractBlock() {
		super(Material.rock);
		this.setHardness(4.0F);
		this.setCreativeTab(CreativeTab.tab1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);
		AbstractMachine tile = (AbstractMachine) world.getTileEntity(x, y, z);
		IIcon f = tile.isBurning() ? front_lit : front;
		if (side == 3 && meta == 0)
			return f;
		switch (side) {
		case 0:
			return bottom;
		case 1:
			return top;
		}
		return (side != meta ? this.side : f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side == 3 && meta == 0)
			return front;
		switch (side) {
		case 0:
			return bottom;
		case 1:
			return top;
		}
		return (side != meta ? this.side : front);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		if (world.getTileEntity(x, y, z) instanceof AbstractMachine) {
			AbstractMachine tile = (AbstractMachine) world.getTileEntity(x, y, z);
			int l = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
			if (l == 0) {
				world.setBlockMetadataWithNotify(x, y, z, 2, 2);
			}
			if (l == 1) {
				world.setBlockMetadataWithNotify(x, y, z, 5, 2);
			}
			if (l == 2) {
				world.setBlockMetadataWithNotify(x, y, z, 3, 2);
			}
			if (l == 3) {
				world.setBlockMetadataWithNotify(x, y, z, 4, 2);
			}
			world.markBlockForUpdate(x, y, z);
		}
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		if (world.getTileEntity(x, y, z) instanceof AbstractMachine) {
			AbstractMachine tile = (AbstractMachine) world.getTileEntity(x, y, z);
			return tile.isBurning() ? 13 : 0;
		}
		return 0;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (world.getTileEntity(x, y, z) instanceof AbstractMachine) {
			AbstractMachine tile = (AbstractMachine) world.getTileEntity(x, y, z);
			for (ItemStack s : tile.getInv()) {
				if (s != null && !world.isRemote) {
					EntityItem ei = new EntityItem(world, x + 0.5d, y + 1, z + 0.5d, s.copy());
					if (s.hasTagCompound())
						ei.getEntityItem().setTagCompound((NBTTagCompound) s.getTagCompound().copy());
					world.spawnEntityInWorld(ei);
				}
			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		if (world.getTileEntity(x, y, z) instanceof AbstractMachine) {
			AbstractMachine tile = (AbstractMachine) world.getTileEntity(x, y, z);
			if (tile.isBurning()) {
				int l = world.getBlockMetadata(x, y, z);
				float f = x + 0.5F;
				float f1 = y + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
				float f2 = z + 0.5F;
				float f3 = 0.52F;
				float f4 = rand.nextFloat() * 0.6F - 0.3F;
				for (int i = 0; i < tile.getSpeed() + 1; i++)
					if (l == 4) {
						world.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
						world.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
					} else if (l == 5) {
						world.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
						world.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
					} else if (l == 2) {
						world.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
						world.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
					} else if (l == 3) {
						world.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
						world.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
					}
			}
		}
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_, int p_149736_5_) {
		return Container.calcRedstoneFromInventory((IInventory) p_149736_1_.getTileEntity(p_149736_2_, p_149736_3_, p_149736_4_));
	}
}
