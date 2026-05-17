package dev.helix.hud;

import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;

public abstract class HudElement {
    private final String id;
    private final String title;
    private int x;
    private int y;
    private double scale = 1.0D;
    private boolean visible = true;
    private boolean rainbow;
    private boolean background = true;
    private int accentColor = RenderUtil.PURPLE;

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

    protected void renderPanel(Minecraft minecraft, String text) {
        RenderUtil.panelText(minecraft, x, y, text, currentAccentColor(), background);
    }

    protected int panelWidth(Minecraft minecraft, String text) {
        return RenderUtil.panelWidth(minecraft, text);
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public void toggleRainbow() {
        rainbow = !rainbow;
    }

    public boolean hasBackground() {
        return background;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    public void toggleBackground() {
        background = !background;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
    }

    public int currentAccentColor() {
        if (!rainbow) {
            return accentColor;
        }
        float offset = Math.abs(id.hashCode() % 360) / 360.0F;
        return RenderUtil.rainbow(offset);
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
