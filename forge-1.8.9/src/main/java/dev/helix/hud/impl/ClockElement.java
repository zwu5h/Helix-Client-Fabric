package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import net.minecraft.client.Minecraft;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class ClockElement extends HudElement {
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    public ClockElement() {
        super("clock", "Clock", 8, 238);
    }

    @Override
    public void render(Minecraft minecraft) {
        renderPanel(minecraft, "TIME " + format.format(new Date()));
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        return panelWidth(minecraft, "TIME 00:00");
    }
}
