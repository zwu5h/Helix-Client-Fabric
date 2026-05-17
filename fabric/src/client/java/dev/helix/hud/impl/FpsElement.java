package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class FpsElement extends HudElement {
    public FpsElement() {
        super("fps", "FPS", 8, 8);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        renderPanel(context, client, "FPS " + client.getCurrentFps());
    }

    @Override
    public int width(MinecraftClient client) {
        return panelWidth(client, "FPS " + client.getCurrentFps());
    }
}
