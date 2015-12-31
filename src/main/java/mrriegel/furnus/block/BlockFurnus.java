package mrriegel.furnus.block;

import java.util.Random;

import mrriegel.furnus.CreativeTab;
import mrriegel.furnus.Furnus;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFurnus extends BlockContainer {
	@SideOnly(Side.CLIENT)
	private IIcon top, side, front, front_lit, bottom;

	public static final Block furnus = new BlockFurnus();

	public BlockFurnus() {
		super(Material.rock);
		this.setHardness(4.0F);
		this.setCreativeTab(CreativeTab.tab1);
		this.setBlockName(Furnus.MODID + ":" + "furnus");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		top = reg.registerIcon(Furnus.MODID + ":furnus_top");
		side = reg.registerIcon(Furnus.MODID + ":furnus_side");
		front = reg.registerIcon(Furnus.MODID + ":furnus_front");
		front_lit = reg.registerIcon(Furnus.MODID + ":furnus_front_lit");
		bottom = reg.registerIcon(Furnus.MODID + ":furnus_bottom");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);
		TileFurnus tile = (TileFurnus) world.getTileEntity(x, y, z);
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
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player,
			ItemStack stack) {
		TileFurnus tile = (TileFurnus) world.getTileEntity(x, y, z);
		int l = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		// if (stack.getTagCompound() != null)
		// tile.readFromNBT(stack.getTagCompound());
		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
			tile.setFace("N");
		}
		if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
			tile.setFace("E");
		}
		if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
			tile.setFace("S");
		}
		if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
			tile.setFace("W");
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
			int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		TileFurnus tile = (TileFurnus) world.getTileEntity(x, y, z);
		if (world.isRemote) {
			return true;
		} else {
			player.openGui(Furnus.instance, 0, world, x, y, z);
			return true;
		}
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileFurnus tile = (TileFurnus) world.getTileEntity(x, y, z);
		return tile.isBurning() ? 13 : 0;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileFurnus tile = (TileFurnus) world.getTileEntity(x, y, z);
		for (ItemStack s : tile.getInv()) {
			if (s != null && !world.isRemote) {
				EntityItem ei = new EntityItem(world, x + 0.5d, y + 1, z + 0.5d, s.copy());
				if (s.hasTagCompound())
					ei.getEntityItem().setTagCompound((NBTTagCompound) s.getTagCompound().copy());
				world.spawnEntityInWorld(ei);
			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	// @Override
	// public void onBlockHarvested(World world, int x, int y, int z, int meta,
	// EntityPlayer player) {
	// if (!player.capabilities.isCreativeMode) {
	// this.dropBlockAsItem(world, x, y, z, meta, 0);
	// }
	// }

	// @Override
	// public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
	// int metadata, int fortune) {
	// ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
	//
	// TileFurnus tile = (TileFurnus) world.getTileEntity(x, y, z);
	// if (tile != null) {
	// ItemStack stack = new ItemStack(this.getItemDropped(metadata, world.rand,
	// fortune), 1,
	// this.damageDropped(metadata));
	//
	// if (stack.getTagCompound() == null) {
	// stack.setTagCompound(new NBTTagCompound());
	// }
	// tile.writeToNBT(stack.getTagCompound());
	//
	// drops.add(stack);
	// }
	// return drops;
	// }

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileFurnus();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		TileFurnus tile = (TileFurnus) world.getTileEntity(x, y, z);
		if (tile.isBurning()) {
			int l = world.getBlockMetadata(x, y, z);
			float f = x + 0.5F;
			float f1 = y + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
			float f2 = z + 0.5F;
			float f3 = 0.52F;
			float f4 = rand.nextFloat() * 0.6F - 0.3F;

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

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_,
			int p_149736_4_, int p_149736_5_) {
		return Container.calcRedstoneFromInventory((IInventory) p_149736_1_.getTileEntity(
				p_149736_2_, p_149736_3_, p_149736_4_));
	}

	public static void init() {
		GameRegistry.registerBlock(furnus, "furnus");
		GameRegistry.registerTileEntity(TileFurnus.class, "tileFurnus");
	}

}
