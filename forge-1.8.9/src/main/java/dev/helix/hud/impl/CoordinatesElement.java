package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public final class CoordinatesElement extends HudElement {
    public CoordinatesElement() {
        super("coordinates", 8, 48);
    }

    @Override
    public void render(Minecraft minecraft) {
        String text = "XYZ "
                + MathHelper.floor_double(minecraft.thePlayer.posX) + " "
                + MathHelper.floor_double(minecraft.thePlayer.posY) + " "
                + MathHelper.floor_double(minecraft.thePlayer.posZ);
        RenderUtil.panelText(minecraft, getX(), getY(), text);
    }
}
