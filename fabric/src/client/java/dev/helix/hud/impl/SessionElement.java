package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class SessionElement extends HudElement {
    private final long startedAt = System.currentTimeMillis();

    public SessionElement() {
        super("session", "Session", 8, 258);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        long seconds = (System.currentTimeMillis() - startedAt) / 1000L;
        renderPanel(context, client, "SES " + (seconds / 60L) + "m");
    }

    @Override
    public int width(MinecraftClient client) {
        return panelWidth(client, "SES 000m");
    }
}
