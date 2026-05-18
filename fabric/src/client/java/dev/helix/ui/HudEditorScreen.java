package dev.helix.ui;

import dev.helix.HelixClient;
import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class HudEditorScreen extends Screen {
    private HudElement dragging;
    private HudElement selected;
    private int dragOffsetX;
    private int dragOffsetY;
    private static final int[] ACCENTS = {0xFF8A35FF, 0xFF35D8FF, 0xFFFF4FD8, 0xFF6CFF91, 0xFFFFD166};

    public HudEditorScreen() {
        super(Text.literal("Helix HUD Editor"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        drawGrid(context);
        MinecraftClient client = MinecraftClient.getInstance();
        for (HudElement element : HelixClient.HUD.elements()) {
            boolean hovered = element.contains(client, mouseX, mouseY);
            int frameColor = element == selected ? 0xAA8A35FF : hovered ? 0x6635D8FF : 0x33101822;
            context.fill(element.x() - 2, element.y() - 2, element.x() + element.scaledWidth(client) + 2, element.y() + element.scaledHeight(client) + 2, frameColor);
            element.renderScaled(context, client);
            if (!element.visible()) {
                context.fill(element.x(), element.y(), element.x() + element.scaledWidth(client), element.y() + element.scaledHeight(client), 0x66000000);
                context.drawText(textRenderer, "hidden", element.x() + 5, element.y() + 4, RenderUtil.MUTED, false);
            }
        }
        context.drawText(textRenderer, "HUD EDITOR", 12, 12, RenderUtil.CYAN, false);
        context.drawText(textRenderer, "Drag | Wheel scale | Right click rainbow | V visible | B bg | C color", 12, 24, RenderUtil.WHITE, false);
        if (selected != null) {
            context.drawText(textRenderer, selected.title() + "  rainbow: " + onOff(selected.rainbow()) + "  visible: " + onOff(selected.visible()), 12, 36, selected.currentAccentColor(), false);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        MinecraftClient client = MinecraftClient.getInstance();
        for (HudElement element : HelixClient.HUD.elements()) {
            if (element.contains(client, (int) click.x(), (int) click.y())) {
                selected = element;
                if (click.button() == 1) {
                    element.toggleRainbow();
                    saveHudEdits();
                    return true;
                }
                if (click.button() == 2) {
                    element.toggleBackground();
                    saveHudEdits();
                    return true;
                }
                dragging = element;
                dragOffsetX = (int) click.x() - element.x();
                dragOffsetY = (int) click.y() - element.y();
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        if (dragging != null) {
            MinecraftClient client = MinecraftClient.getInstance();
            int snappedX = Math.max(2, Math.min(width - dragging.scaledWidth(client) - 2, (int) click.x() - dragOffsetX));
            int snappedY = Math.max(2, Math.min(height - dragging.scaledHeight(client) - 2, (int) click.y() - dragOffsetY));
            dragging.setPosition((snappedX / 4) * 4, (snappedY / 4) * 4);
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        dragging = null;
        saveHudEdits();
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        MinecraftClient client = MinecraftClient.getInstance();
        for (HudElement element : HelixClient.HUD.elements()) {
            if (element.contains(client, (int) mouseX, (int) mouseY)) {
                element.setScale(element.scale() + (verticalAmount > 0 ? 0.1D : -0.1D));
                saveHudEdits();
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == GLFW.GLFW_KEY_H || input.key() == GLFW.GLFW_KEY_ESCAPE) {
            saveHudEdits();
            close();
            return true;
        }
        if (selected != null && input.key() == GLFW.GLFW_KEY_R) {
            selected.toggleRainbow();
            saveHudEdits();
            return true;
        }
        if (selected != null && input.key() == GLFW.GLFW_KEY_V) {
            selected.setVisible(!selected.visible());
            saveHudEdits();
            return true;
        }
        if (selected != null && input.key() == GLFW.GLFW_KEY_B) {
            selected.toggleBackground();
            saveHudEdits();
            return true;
        }
        if (selected != null && input.key() == GLFW.GLFW_KEY_C) {
            selected.setAccentColor(nextAccent(selected.accentColor()));
            selected.setRainbow(false);
            saveHudEdits();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void drawGrid(DrawContext context) {
        for (int x = 0; x < width; x += 16) {
            context.fill(x, 0, x + 1, height, 0x22101A24);
        }
        for (int y = 0; y < height; y += 16) {
            context.fill(0, y, width, y + 1, 0x22101A24);
        }
    }

    private int nextAccent(int current) {
        for (int i = 0; i < ACCENTS.length; i++) {
            if (ACCENTS[i] == current) {
                return ACCENTS[(i + 1) % ACCENTS.length];
            }
        }
        return ACCENTS[0];
    }

    private String onOff(boolean value) {
        return value ? "ON" : "OFF";
    }

    private void saveHudEdits() {
        HelixClient.MODULES.syncHudSettingsFromElements();
        HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
    }
}
