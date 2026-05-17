package dev.helix.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class HudElement {
    private final String id;
    private int x;
    private int y;
    private double scale = 1.0;

    protected HudElement(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public abstract void render(DrawContext context, MinecraftClient client);

    public void tick(MinecraftClient client) {
    }

    public String id() {
        return id;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public double scale() {
        return scale;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setScale(double scale) {
        this.scale = Math.max(0.5, Math.min(3.0, scale));
    }
}
