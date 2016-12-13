package mrriegel.furnus.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPulvus extends AbstractBlock<TilePulvus> {

	public BlockPulvus() {
		super("pulvus");
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePulvus();
	}

	@Override
	protected Class<? extends TilePulvus> getTile() {
		return TilePulvus.class;
	}

}
