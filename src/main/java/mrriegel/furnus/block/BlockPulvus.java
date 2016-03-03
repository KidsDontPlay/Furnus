package mrriegel.furnus.block;

import mrriegel.furnus.Furnus;
import mrriegel.furnus.handler.GuiHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPulvus extends AbstractBlock {

	public BlockPulvus() {
		super();
		this.setBlockName(Furnus.MODID + ":" + "pulvus");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		top = reg.registerIcon(Furnus.MODID + ":pulvus_top");
		side = reg.registerIcon(Furnus.MODID + ":pulvus_side");
		front = reg.registerIcon(Furnus.MODID + ":pulvus_front");
		front_lit = reg.registerIcon(Furnus.MODID + ":pulvus_front_lit");
		bottom = reg.registerIcon(Furnus.MODID + ":pulvus_bottom");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		TilePulvus tile = (TilePulvus) world.getTileEntity(x, y, z);
		if (world.isRemote) {
			return true;
		} else {
			player.openGui(Furnus.instance, GuiHandler.PULVUS, world, x, y, z);
			return true;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TilePulvus();
	}

}
