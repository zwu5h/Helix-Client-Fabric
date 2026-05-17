package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public final class DirectionElement extends HudElement {
    private static final String[] DIRECTIONS = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};

    public DirectionElement() {
        super("direction", "Direction", 8, 178);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        int index = MathHelper.floor((client.player.getYaw() * 8.0F / 360.0F) + 0.5D) & 7;
        RenderUtil.panelText(context, client, x(), y(), "DIR " + DIRECTIONS[index]);
    }

    @Override
    public int width(MinecraftClient client) {
        return RenderUtil.panelWidth(client, "DIR NW");
    }
}
