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
        int color = hovered ? RenderUtil.CYAN : RenderUtil.WHITE;
        minecraft.fontRendererObj.drawString(displayString, xPosition + width / 2 - minecraft.fontRendererObj.getStringWidth(displayString) / 2, yPosition + height / 2 - 4, color);
        if (hovered) {
            Gui.drawRect(xPosition + 14, yPosition + height - 4, xPosition + width - 14, yPosition + height - 3, 0xAA35D8FF);
        }
    }
}
