package dev.helix.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class RenderUtil {
    public static final int PANEL = 0xAA0C1016;
    public static final int PANEL_HOVER = 0xCC102532;
    public static final int CYAN = 0xFF35D8FF;
    public static final int PURPLE = 0xFF8A35FF;
    public static final int WHITE = 0xFFEAF6FF;
    public static final int MUTED = 0xFF98A8B3;

    private RenderUtil() {
    }

    public static void panelText(DrawContext context, MinecraftClient client, int x, int y, String text) {
        panelText(context, client, x, y, text, CYAN, true);
    }

    public static void panelText(DrawContext context, MinecraftClient client, int x, int y, String text, int accent, boolean background) {
        panelText(context, client, x, y, text, accent, WHITE, background, 0xAA, true);
    }

    public static void panelText(DrawContext context, MinecraftClient client, int x, int y, String text, int accent, int textColor, boolean background, int backgroundAlpha, boolean border) {
        int width = panelWidth(client, text);
        if (background) {
            context.fill(x, y, x + width, y + 16, (backgroundAlpha << 24) | 0x0C1016);
        }
        if (border) {
            context.fill(x, y + 15, x + width, y + 16, accent);
        }
        context.drawText(client.textRenderer, text, x + 7, y + 4, textColor, false);
    }

    public static int panelWidth(MinecraftClient client, String text) {
        return client.textRenderer.getWidth(text) + 14;
    }

    public static void glassPanel(DrawContext context, int x, int y, int width, int height, boolean hovered) {
        context.fill(x, y, x + width, y + height, hovered ? 0xAA143342 : 0x88101822);
        context.fill(x, y, x + width, y + 1, 0x88EAF6FF);
        context.fill(x, y + height - 1, x + width, y + height, 0x5535D8FF);
    }

    public static void key(DrawContext context, MinecraftClient client, int x, int y, String label, boolean active) {
        context.fill(x, y, x + 20, y + 18, active ? PANEL_HOVER : PANEL);
        context.drawText(client.textRenderer, label, x + 7, y + 5, active ? CYAN : WHITE, false);
    }

    public static int rainbow(float offset) {
        float hue = ((System.currentTimeMillis() % 5000L) / 5000.0F + offset) % 1.0F;
        return 0xFF000000 | java.awt.Color.HSBtoRGB(hue, 0.78F, 1.0F) & 0x00FFFFFF;
    }
}
