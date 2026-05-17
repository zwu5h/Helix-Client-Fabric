package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import net.minecraft.client.Minecraft;

public final class MemoryElement extends HudElement {
    public MemoryElement() {
        super("memory", "Memory", 8, 218);
    }

    @Override
    public void render(Minecraft minecraft) {
        Runtime runtime = Runtime.getRuntime();
        long used = (runtime.totalMemory() - runtime.freeMemory()) / 1024L / 1024L;
        renderPanel(minecraft, "MEM " + used + "MB");
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        return panelWidth(minecraft, "MEM 0000MB");
    }
}
