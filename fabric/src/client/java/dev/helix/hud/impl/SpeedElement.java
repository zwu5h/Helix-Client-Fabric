package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class SpeedElement extends HudElement {
    public SpeedElement() {
        super("speed", "Speed", 8, 198);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        double dx = client.player.getX() - client.player.lastX;
        double dz = client.player.getZ() - client.player.lastZ;
        double blocksPerSecond = Math.sqrt(dx * dx + dz * dz) * 20.0D;
        renderPanel(context, client, String.format("SPD %.2f", blocksPerSecond));
    }

    @Override
    public int width(MinecraftClient client) {
        return panelWidth(client, "SPD 00.00");
    }
}
