package dev.helix.ui;

import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

public final class HelixButton extends GuiButton {
    public HelixButton(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    @Override
    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        if (!visible) {
            return;
        }

        hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
        RenderUtil.glassPanel(xPosition, yPosition, width, height, hovered);
        Gui.drawRect(xPosition, yPosition, xPosition + 1, yPosition + height, hovered ? 0xCCB45CFF : 0x665A25B8);
        Gui.drawRect(xPosition + width - 1, yPosition, xPosition + width, yPosition + height, hovered ? 0xCCB45CFF : 0x665A25B8);
        int color = hovered ? RenderUtil.CYAN : RenderUtil.WHITE;
        drawIcon();
        minecraft.fontRendererObj.drawString(displayString, xPosition + 86, yPosition + height / 2 - 4, color);
        if (hovered) {
            Gui.drawRect(xPosition + 1, yPosition + 1, xPosition + width - 1, yPosition + 2, 0x99B45CFF);
        }
    }

    private void drawIcon() {
        int cx = xPosition + 42;
        int cy = yPosition + height / 2;
        int purple = hovered ? 0xFFB45CFF : 0xFF7A34E8;
        if (id == 1) {
            Gui.drawRect(cx - 5, cy - 8, cx - 2, cy + 8, purple);
            Gui.drawRect(cx - 2, cy - 5, cx + 4, cy + 5, purple);
            Gui.drawRect(cx + 4, cy - 2, cx + 7, cy + 2, purple);
        } else if (id == 2) {
            Gui.drawRect(cx - 10, cy - 2, cx - 4, cy + 7, purple);
            Gui.drawRect(cx - 7, cy - 9, cx - 1, cy - 3, purple);
            Gui.drawRect(cx + 2, cy - 2, cx + 8, cy + 7, purple);
            Gui.drawRect(cx + 1, cy - 9, cx + 7, cy - 3, purple);
        } else if (id == 3) {
            Gui.drawRect(cx - 7, cy - 7, cx + 7, cy + 7, purple);
            Gui.drawRect(cx - 3, cy - 3, cx + 3, cy + 3, 0xAA0B0616);
        } else {
            Gui.drawRect(cx - 9, cy - 9, cx + 5, cy + 9, purple);
            Gui.drawRect(cx - 6, cy - 6, cx + 2, cy + 6, 0xAA0B0616);
            Gui.drawRect(cx + 2, cy - 2, cx + 10, cy + 2, purple);
        }
    }
}
