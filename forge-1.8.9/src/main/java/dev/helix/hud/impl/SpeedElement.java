package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;

public final class SpeedElement extends HudElement {
    public SpeedElement() {
        super("speed", "Speed", 8, 198);
    }

    @Override
    public void render(Minecraft minecraft) {
        double dx = minecraft.thePlayer.posX - minecraft.thePlayer.prevPosX;
        double dz = minecraft.thePlayer.posZ - minecraft.thePlayer.prevPosZ;
        double blocksPerSecond = Math.sqrt(dx * dx + dz * dz) * 20.0D;
        renderPanel(minecraft, String.format("SPD %.2f", blocksPerSecond));
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        return panelWidth(minecraft, "SPD 00.00");
    }
}
