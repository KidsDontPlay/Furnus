package mrriegel.furnus.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFurnus extends AbstractBlock<TileFurnus> {

	public BlockFurnus() {
		super("furnus");
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileFurnus();
	}

	@Override
	protected Class<? extends TileFurnus> getTile() {
		return TileFurnus.class;
	}

}
