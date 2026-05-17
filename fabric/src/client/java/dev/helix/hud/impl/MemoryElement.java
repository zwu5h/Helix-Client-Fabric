package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class MemoryElement extends HudElement {
    public MemoryElement() {
        super("memory", "Memory", 8, 218);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        Runtime runtime = Runtime.getRuntime();
        long used = (runtime.totalMemory() - runtime.freeMemory()) / 1024L / 1024L;
        renderPanel(context, client, "MEM " + used + "MB");
    }

    @Override
    public int width(MinecraftClient client) {
        return panelWidth(client, "MEM 0000MB");
    }
}
