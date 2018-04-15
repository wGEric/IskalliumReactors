package zairus.iskalliumreactors.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import zairus.iskalliumreactors.tileentity.TileBase;

public class MessageTile extends MessageBase {

    public MessageTile() {

    }

    public MessageTile(TileBase tileBase) {
        BlockPos pos = tileBase.getPos();
        addInt(pos.getX());
        addInt(pos.getY());
        addInt(pos.getZ());
    }

    public static class MessageTileHandler implements IMessageHandler<MessageTile, IMessage> {
        @Override
        public IMessage onMessage(MessageTile message, MessageContext ctx) {
            if (ctx.side != Side.CLIENT)
                return null;

            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.addScheduledTask(() -> {
                TileEntity entity = minecraft.world.getTileEntity(new BlockPos(message.getInt(), message.getInt(), message.getInt()));

                if (entity instanceof TileEntity) {
                    ((TileBase) entity).handleMessageTile(message);
                }
            });
            return null;
        }
    }
}
