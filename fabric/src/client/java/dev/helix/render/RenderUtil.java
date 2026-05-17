package dev.helix.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class RenderUtil {
    public static final int PANEL = 0xAA0C1016;
    public static final int PANEL_HOVER = 0xCC102532;
    public static final int CYAN = 0xFF35D8FF;
    public static final int WHITE = 0xFFEAF6FF;
    public static final int MUTED = 0xFF98A8B3;

    private RenderUtil() {
    }

    public static void panelText(DrawContext context, MinecraftClient client, int x, int y, String text) {
        int width = client.textRenderer.getWidth(text) + 14;
        context.fill(x, y, x + width, y + 16, PANEL);
        context.fill(x, y + 15, x + width, y + 16, CYAN);
        context.drawText(client.textRenderer, text, x + 7, y + 4, WHITE, false);
    }

    public static void key(DrawContext context, MinecraftClient client, int x, int y, String label, boolean active) {
        context.fill(x, y, x + 20, y + 18, active ? PANEL_HOVER : PANEL);
        context.drawText(client.textRenderer, label, x + 7, y + 5, active ? CYAN : WHITE, false);
    }
}
