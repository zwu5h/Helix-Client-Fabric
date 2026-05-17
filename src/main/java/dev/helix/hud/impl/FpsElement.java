package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;

public final class FpsElement extends HudElement {
    public FpsElement() {
        super("fps", 8, 8);
    }

    @Override
    public void render(Minecraft minecraft) {
        RenderUtil.panelText(minecraft, getX(), getY(), "FPS " + Minecraft.getDebugFPS());
    }
}
