package dev.helix.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public final class RenderUtil {
    public static final int PANEL = 0xAA0C1016;
    public static final int PANEL_HOVER = 0xCC102532;
    public static final int CYAN = 0xFF35D8FF;
    public static final int WHITE = 0xFFEAF6FF;
    public static final int MUTED = 0xFF98A8B3;

    private RenderUtil() {
    }

    public static void panelText(Minecraft minecraft, int x, int y, String text) {
        int width = minecraft.fontRendererObj.getStringWidth(text) + 14;
        Gui.drawRect(x, y, x + width, y + 16, PANEL);
        Gui.drawRect(x, y + 15, x + width, y + 16, CYAN);
        minecraft.fontRendererObj.drawString(text, x + 7, y + 4, WHITE, false);
    }

    public static void key(Minecraft minecraft, int x, int y, String label, boolean active) {
        Gui.drawRect(x, y, x + 20, y + 18, active ? PANEL_HOVER : PANEL);
        minecraft.fontRendererObj.drawString(label, x + 7, y + 5, active ? CYAN : WHITE, false);
    }
}
