package dev.helix.hud;

import net.minecraft.client.Minecraft;

public abstract class HudElement {
    private final String id;
    private int x;
    private int y;
    private double scale = 1.0D;

    protected HudElement(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public abstract void render(Minecraft minecraft);

    public void tick(Minecraft minecraft) {
    }

    public String getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getScale() {
        return scale;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setScale(double scale) {
        this.scale = Math.max(0.5D, Math.min(3.0D, scale));
    }
}
