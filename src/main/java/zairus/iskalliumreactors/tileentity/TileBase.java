package zairus.iskalliumreactors.tileentity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.tileentity.TileEntity;
import zairus.iskalliumreactors.IskalliumReactors;
import zairus.iskalliumreactors.networking.MessageTile;

public abstract class TileBase extends TileEntity {
    public Object getGuiClient(InventoryPlayer inventoryPlayer) {
        return null;
    }

    public Object getGuiServer(InventoryPlayer inventoryPlayer) {
        return null;
    }

    public void sendGuiNetworkData(IContainerListener listener) {
        if (listener instanceof EntityPlayerMP) {
            MessageTile guiMessage = getGuiMessage();
            if (guiMessage != null) {
                IskalliumReactors.networkWrapper.sendTo(guiMessage, (EntityPlayerMP) listener);
            }
        }
    }

    public MessageTile getGuiMessage() {
       return new MessageTile(this);
    }

    public void handleMessageTile(MessageTile message) {

    }
}
