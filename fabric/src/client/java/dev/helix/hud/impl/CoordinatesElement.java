package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class CoordinatesElement extends HudElement {
    public CoordinatesElement() {
        super("coordinates", "Coordinates", 8, 48);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        String text = "XYZ " + client.player.getBlockX() + " " + client.player.getBlockY() + " " + client.player.getBlockZ();
        RenderUtil.panelText(context, client, x(), y(), text);
    }

    @Override
    public int width(MinecraftClient client) {
        return RenderUtil.panelWidth(client, "XYZ -0000 000 -0000");
    }
}
