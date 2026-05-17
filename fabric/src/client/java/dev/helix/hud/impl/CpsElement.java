package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayDeque;
import java.util.Deque;

public final class CpsElement extends HudElement {
    private final Deque<Long> clicks = new ArrayDeque<>();

    public CpsElement() {
        super("cps", "CPS", 8, 28);
    }

    @Override
    public void tick(MinecraftClient client) {
        while (client.options.attackKey.wasPressed()) {
            clicks.addLast(System.currentTimeMillis());
        }

        long cutoff = System.currentTimeMillis() - 1_000L;
        while (!clicks.isEmpty() && clicks.peekFirst() < cutoff) {
            clicks.removeFirst();
        }
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        renderPanel(context, client, "CPS " + clicks.size());
    }

    @Override
    public int width(MinecraftClient client) {
        return panelWidth(client, "CPS 00");
    }
}
