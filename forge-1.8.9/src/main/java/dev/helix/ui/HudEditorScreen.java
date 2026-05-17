package dev.helix.ui;

import dev.helix.HelixClient;
import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public final class HudEditorScreen extends GuiScreen {
    private HudElement dragging;
    private HudElement selected;
    private int dragOffsetX;
    private int dragOffsetY;
    private static final int[] ACCENTS = {0xFF8A35FF, 0xFF35D8FF, 0xFFFF4FD8, 0xFF6CFF91, 0xFFFFD166};

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawGrid();
        for (HudElement element : HelixClient.HUD.elements()) {
            boolean hovered = element.contains(mc, mouseX, mouseY);
            int frameColor = element == selected ? 0xAA8A35FF : hovered ? 0x6635D8FF : 0x33101822;
            Gui.drawRect(element.getX() - 2, element.getY() - 2, element.getX() + element.getWidth(mc) + 2, element.getY() + element.getHeight(mc) + 2, frameColor);
            element.render(mc);
            if (!element.isVisible()) {
                Gui.drawRect(element.getX(), element.getY(), element.getX() + element.getWidth(mc), element.getY() + element.getHeight(mc), 0x66000000);
                fontRendererObj.drawString("hidden", element.getX() + 5, element.getY() + 4, RenderUtil.MUTED);
            }
        }
        fontRendererObj.drawString("HUD EDITOR", 12, 12, RenderUtil.CYAN);
        fontRendererObj.drawString("Drag | Wheel scale | Right click rainbow | V visible | B bg | C color", 12, 24, RenderUtil.WHITE);
        if (selected != null) {
            fontRendererObj.drawString(selected.getTitle() + "  rainbow: " + onOff(selected.isRainbow()) + "  visible: " + onOff(selected.isVisible()), 12, 36, selected.currentAccentColor());
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (HudElement element : HelixClient.HUD.elements()) {
            if (element.contains(mc, mouseX, mouseY)) {
                selected = element;
                if (mouseButton == 1) {
                    element.toggleRainbow();
                    HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
                    return;
                }
                if (mouseButton == 2) {
                    element.toggleBackground();
                    HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
                    return;
                }
                dragging = element;
                dragOffsetX = mouseX - element.getX();
                dragOffsetY = mouseY - element.getY();
                return;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (dragging != null) {
            int snappedX = Math.max(2, Math.min(width - dragging.getWidth(mc) - 2, mouseX - dragOffsetX));
            int snappedY = Math.max(2, Math.min(height - dragging.getHeight(mc) - 2, mouseY - dragOffsetY));
            dragging.setPosition((snappedX / 4) * 4, (snappedY / 4) * 4);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = null;
        HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = org.lwjgl.input.Mouse.getEventDWheel();
        if (wheel != 0) {
            HudElement hovered = hoveredElement();
            if (hovered != null) {
                hovered.setScale(hovered.getScale() + (wheel > 0 ? 0.1D : -0.1D));
                HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_H || keyCode == Keyboard.KEY_ESCAPE) {
            HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
            mc.displayGuiScreen(null);
            return;
        }
        if (selected != null && keyCode == Keyboard.KEY_R) {
            selected.toggleRainbow();
            HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
            return;
        }
        if (selected != null && keyCode == Keyboard.KEY_V) {
            selected.setVisible(!selected.isVisible());
            HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
            return;
        }
        if (selected != null && keyCode == Keyboard.KEY_B) {
            selected.toggleBackground();
            HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
            return;
        }
        if (selected != null && keyCode == Keyboard.KEY_C) {
            selected.setAccentColor(nextAccent(selected.getAccentColor()));
            selected.setRainbow(false);
            HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private HudElement hoveredElement() {
        int mouseX = org.lwjgl.input.Mouse.getEventX() * width / Minecraft.getMinecraft().displayWidth;
        int mouseY = height - org.lwjgl.input.Mouse.getEventY() * height / Minecraft.getMinecraft().displayHeight - 1;
        for (HudElement element : HelixClient.HUD.elements()) {
            if (element.contains(mc, mouseX, mouseY)) {
                return element;
            }
        }
        return null;
    }

    private void drawGrid() {
        for (int x = 0; x < width; x += 16) {
            Gui.drawRect(x, 0, x + 1, height, 0x22101A24);
        }
        for (int y = 0; y < height; y += 16) {
            Gui.drawRect(0, y, width, y + 1, 0x22101A24);
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
}
