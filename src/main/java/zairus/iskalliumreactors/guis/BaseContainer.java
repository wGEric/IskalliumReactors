package zairus.iskalliumreactors.guis;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import zairus.iskalliumreactors.tileentity.TileBase;

public class BaseContainer extends Container {
    private TileBase tileBase;

    public BaseContainer(InventoryPlayer inventoryPlayer, TileBase tileBase) {
        bindPlayerInventory(inventoryPlayer);
        this.tileBase = tileBase;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
       super.detectAndSendChanges();

        for (IContainerListener listener : listeners) {
            tileBase.sendGuiNetworkData(listener);
        }
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        int xOffset = 8;
        int yOffset = 84;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
        }
    }
}
