package dev.helix.hud;

import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class HudElement {
    private final String id;
    private final String title;
    private int x;
    private int y;
    private double scale = 1.0;
    private boolean visible = true;
    private boolean rainbow;
    private boolean background = true;
    private boolean border = true;
    private int backgroundAlpha = 0xAA;
    private int accentColor = RenderUtil.PURPLE;
    private int textColor = RenderUtil.WHITE;

    protected HudElement(String id, String title, int x, int y) {
        this.id = id;
        this.title = title;
        this.x = x;
        this.y = y;
    }

    public abstract void render(DrawContext context, MinecraftClient client);

    public abstract int width(MinecraftClient client);

    public int height(MinecraftClient client) {
        return 16;
    }

    public final void renderScaled(DrawContext context, MinecraftClient client) {
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        context.getMatrices().scale((float) scale, (float) scale);
        context.getMatrices().translate(-x, -y);
        render(context, client);
        context.getMatrices().popMatrix();
    }

    public void tick(MinecraftClient client) {
    }

    protected void renderPanel(DrawContext context, MinecraftClient client, String text) {
        RenderUtil.panelText(context, client, x, y, text, currentAccentColor(), textColor, background, backgroundAlpha, border);
    }

    protected int panelWidth(MinecraftClient client, String text) {
        return RenderUtil.panelWidth(client, text);
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
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

    public boolean visible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean rainbow() {
        return rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public void toggleRainbow() {
        rainbow = !rainbow;
    }

    public boolean background() {
        return background;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    public void toggleBackground() {
        background = !background;
    }

    public int accentColor() {
        return accentColor;
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
    }

    public boolean border() {
        return border;
    }

    public void setBorder(boolean border) {
        this.border = border;
    }

    public int backgroundAlpha() {
        return backgroundAlpha;
    }

    public void setBackgroundAlpha(int backgroundAlpha) {
        this.backgroundAlpha = Math.max(0, Math.min(255, backgroundAlpha));
    }

    public int textColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
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

    public void setScale(double scale) {
        this.scale = Math.max(0.5, Math.min(3.0, scale));
    }

    public boolean contains(MinecraftClient client, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + scaledWidth(client) && mouseY >= y && mouseY <= y + scaledHeight(client);
    }

    public int scaledWidth(MinecraftClient client) {
        return (int) Math.ceil(width(client) * scale);
    }

    public int scaledHeight(MinecraftClient client) {
        return (int) Math.ceil(height(client) * scale);
    }
}
