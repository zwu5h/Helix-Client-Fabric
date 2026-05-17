package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class ClockElement extends HudElement {
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    public ClockElement() {
        super("clock", "Clock", 8, 238);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        renderPanel(context, client, "TIME " + format.format(new Date()));
    }

    @Override
    public int width(MinecraftClient client) {
        return panelWidth(client, "TIME 00:00");
    }
}
