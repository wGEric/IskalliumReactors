package zairus.iskalliumreactors.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zairus.iskalliumreactors.IskalliumReactors;
import zairus.iskalliumreactors.handlres.IRGuiHandler;
import zairus.iskalliumreactors.tileentity.TileBase;

public abstract class BlockIRBase extends Block implements ITileEntityProvider, IBlockTileEntity {
    public BlockIRBase(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    public BlockIRBase(Material materialIn) {
        super(materialIn);
    }

    @Override
    public boolean onBlockActivated(World worldIn,
                                    BlockPos pos,
                                    IBlockState state,
                                    EntityPlayer playerIn,
                                    EnumHand hand,
                                    EnumFacing side,
                                    float hitX,
                                    float hitY,
                                    float hit)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileBase && !worldIn.isRemote)
        {
            playerIn.openGui(IskalliumReactors.instance, IRGuiHandler.TILE_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }
}
