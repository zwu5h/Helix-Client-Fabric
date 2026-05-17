package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;

public final class KeystrokesElement extends HudElement {
    public KeystrokesElement() {
        super("keystrokes", "Keystrokes", 8, 72);
    }

    @Override
    public void render(Minecraft minecraft) {
        int x = getX();
        int y = getY();
        RenderUtil.key(minecraft, x + 24, y, "W", minecraft.gameSettings.keyBindForward.isKeyDown());
        RenderUtil.key(minecraft, x, y + 22, "A", minecraft.gameSettings.keyBindLeft.isKeyDown());
        RenderUtil.key(minecraft, x + 24, y + 22, "S", minecraft.gameSettings.keyBindBack.isKeyDown());
        RenderUtil.key(minecraft, x + 48, y + 22, "D", minecraft.gameSettings.keyBindRight.isKeyDown());
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        return 68;
    }

    @Override
    public int getHeight(Minecraft minecraft) {
        return 40;
    }
}
