package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;

public final class FpsElement extends HudElement {
    public FpsElement() {
        super("fps", "FPS", 8, 8);
    }

    @Override
    public void render(Minecraft minecraft) {
        renderPanel(minecraft, "FPS " + Minecraft.getDebugFPS());
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        return panelWidth(minecraft, "FPS " + Minecraft.getDebugFPS());
    }
}
