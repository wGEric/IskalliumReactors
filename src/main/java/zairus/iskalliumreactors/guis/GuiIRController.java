package zairus.iskalliumreactors.guis;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import zairus.iskalliumreactors.IRConstants;
import zairus.iskalliumreactors.IskalliumReactors;
import zairus.iskalliumreactors.tileentity.TileEntityIRController;

public class GuiIRController extends GuiContainer {
    public static final String TEX_PATH = IRConstants.PATH_GUI_STORAGE + "controller.png";
    public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

    public TileEntityIRController tileEntityIRController;

    public GuiIRController(InventoryPlayer inventoryPlayer, TileEntityIRController tileEntityIRController) {
        super(new BaseContainer(inventoryPlayer, tileEntityIRController));
        this.tileEntityIRController = tileEntityIRController;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1, 1);

        bindTexture(TEXTURE);

        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawEnergyCell();

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 0.0F);
        GlStateManager.popMatrix();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        String title = tileEntityIRController.getBlockType().getLocalizedName();
        fontRenderer.drawString(title, getCenteredOffset(title, 87), 5, 0x404040);

        String send = I18n.format("info.iskalliumReactors.controller.generating", tileEntityIRController.getReactorYield());
        fontRenderer.drawSplitString(send, 35, 35, 130, 0x404040);

        super.drawGuiContainerForegroundLayer(x, y);
    }

    private void bindTexture(ResourceLocation texture) {
        mc.renderEngine.bindTexture(texture);
    }

    private int getCenteredOffset(String string, int xPos) {
        return ((xPos * 2) - fontRenderer.getStringWidth(string)) / 2;
    }

    private void drawEnergyCell() {
        int sizeX = 9;
        int sizeY = 59;
        int amount = getScaled(sizeY);
        int posX = guiLeft + 16;
        int posY = guiTop + 14 + sizeY - amount;

        IskalliumReactors.logger.info("y: " + posY);
        IskalliumReactors.logger.info("amount: " + amount);
        IskalliumReactors.logger.info("stored: " + tileEntityIRController.getEnergyStored());
        IskalliumReactors.logger.info("capacity: " + tileEntityIRController.getMaxEnergyStored());

        drawTexturedModalRect(posX, posY, 176, 0, sizeX, amount - 1);
    }

    private int getScaled(int sizeY) {

        if (tileEntityIRController.getMaxEnergyStored() <= 0) {
            return sizeY;
        }

        long fraction = (long) tileEntityIRController.getEnergyStored() * sizeY / tileEntityIRController.getMaxEnergyStored();
        return Math.round(fraction);
    }
}
