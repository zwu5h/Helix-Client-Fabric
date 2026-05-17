package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public final class CoordinatesElement extends HudElement {
    public CoordinatesElement() {
        super("coordinates", "Coordinates", 8, 48);
    }

    @Override
    public void render(Minecraft minecraft) {
        String text = "XYZ "
                + MathHelper.floor_double(minecraft.thePlayer.posX) + " "
                + MathHelper.floor_double(minecraft.thePlayer.posY) + " "
                + MathHelper.floor_double(minecraft.thePlayer.posZ);
        renderPanel(minecraft, text);
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        return panelWidth(minecraft, "XYZ -0000 000 -0000");
    }
}
