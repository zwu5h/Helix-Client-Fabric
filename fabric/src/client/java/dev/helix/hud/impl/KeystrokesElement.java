package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class KeystrokesElement extends HudElement {
    public KeystrokesElement() {
        super("keystrokes", "Keystrokes", 8, 72);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        int x = x();
        int y = y();
        RenderUtil.key(context, client, x + 24, y, "W", client.options.forwardKey.isPressed());
        RenderUtil.key(context, client, x, y + 22, "A", client.options.leftKey.isPressed());
        RenderUtil.key(context, client, x + 24, y + 22, "S", client.options.backKey.isPressed());
        RenderUtil.key(context, client, x + 48, y + 22, "D", client.options.rightKey.isPressed());
    }

    @Override
    public int width(MinecraftClient client) {
        return 68;
    }

    @Override
    public int height(MinecraftClient client) {
        return 40;
    }
}
