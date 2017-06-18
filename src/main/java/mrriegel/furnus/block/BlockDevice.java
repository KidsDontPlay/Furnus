package mrriegel.furnus.block;

import static net.minecraft.block.BlockDirectional.FACING;
import static net.minecraft.block.BlockLever.POWERED;

import java.util.Random;

import mrriegel.furnus.util.CreativeTab;
import mrriegel.limelib.block.CommonBlockContainer;
import mrriegel.limelib.tile.CommonTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDevice<T extends CommonTile> extends CommonBlockContainer<T> {

	private final Class<? extends T> clazz;

	public BlockDevice(String name, Class<? extends T> clazz) {
		super(Material.ROCK, name);
		setHardness(3f);
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
		setCreativeTab(CreativeTab.tab);
		this.clazz = clazz;
	}

	@Override
	protected Class<? extends T> getTile() {
		return clazz;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, POWERED });
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta % 6);
		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
			enumfacing = EnumFacing.NORTH;
		return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, meta > 5);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int p = state.getValue(POWERED) ? 0 : 6;
		return state.getValue(FACING).getIndex() + p;
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(POWERED, false).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public int getLightValue(IBlockState state) {
		return state.getValue(POWERED) ? 13 : 0;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand) {
		if (state.getValue(POWERED)) {
			EnumFacing enumfacing = state.getValue(FACING);
			double d0 = pos.getX() + 0.5D;
			double d1 = pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			double d2 = pos.getZ() + 0.5D;
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			int speed = 0;
			//				TODO
			//				AbstractMachine tile = (AbstractMachine) worldIn.getTileEntity(pos);
			//				speed=tile.getSpeed();
			for (int i = 0; i < speed + 1; i++)
				switch (enumfacing) {
				case WEST:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					break;
				case EAST:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					break;
				case NORTH:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
					break;
				case SOUTH:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
				default:
					break;
				}
		}
	}

}
