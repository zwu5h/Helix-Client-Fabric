package dev.helix.hud;

import net.minecraft.client.Minecraft;

public abstract class HudElement {
    private final String id;
    private final String title;
    private int x;
    private int y;
    private double scale = 1.0D;

    protected HudElement(String id, String title, int x, int y) {
        this.id = id;
        this.title = title;
        this.x = x;
        this.y = y;
    }

    public abstract void render(Minecraft minecraft);

    public abstract int getWidth(Minecraft minecraft);

    public int getHeight(Minecraft minecraft) {
        return 16;
    }

    public void tick(Minecraft minecraft) {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    public boolean contains(Minecraft minecraft, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + getWidth(minecraft) && mouseY >= y && mouseY <= y + getHeight(minecraft);
    }

    public void setScale(double scale) {
        this.scale = Math.max(0.5D, Math.min(3.0D, scale));
    }
}
