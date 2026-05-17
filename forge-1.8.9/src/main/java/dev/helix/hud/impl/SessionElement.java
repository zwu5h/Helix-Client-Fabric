package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import net.minecraft.client.Minecraft;

public final class SessionElement extends HudElement {
    private final long startedAt = System.currentTimeMillis();

    public SessionElement() {
        super("session", "Session", 8, 258);
    }

    @Override
    public void render(Minecraft minecraft) {
        long seconds = (System.currentTimeMillis() - startedAt) / 1000L;
        renderPanel(minecraft, "SES " + (seconds / 60L) + "m");
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        return panelWidth(minecraft, "SES 000m");
    }
}
