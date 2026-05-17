package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public final class DirectionElement extends HudElement {
    private static final String[] DIRECTIONS = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};

    public DirectionElement() {
        super("direction", "Direction", 8, 178);
    }

    @Override
    public void render(Minecraft minecraft) {
        int index = MathHelper.floor_double((minecraft.thePlayer.rotationYaw * 8.0F / 360.0F) + 0.5D) & 7;
        RenderUtil.panelText(minecraft, getX(), getY(), "DIR " + DIRECTIONS[index]);
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        return RenderUtil.panelWidth(minecraft, "DIR NW");
    }
}
