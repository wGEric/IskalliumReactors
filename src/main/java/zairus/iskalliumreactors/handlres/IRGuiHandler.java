package zairus.iskalliumreactors.handlres;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import zairus.iskalliumreactors.tileentity.TileBase;

import javax.annotation.Nullable;

public class IRGuiHandler implements IGuiHandler {
    public static final int TILE_ID = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case TILE_ID:
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                if (tile instanceof TileBase) {
                    return ((TileBase) tile).getGuiServer(player.inventory);
                }
                return null;
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case TILE_ID:
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                if (tile instanceof TileBase) {
                    return ((TileBase) tile).getGuiClient(player.inventory);
                }
                return null;
        }
        return null;
    }
}
