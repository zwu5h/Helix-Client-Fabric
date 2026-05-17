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
    private int dragOffsetX;
    private int dragOffsetY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawGrid();
        for (HudElement element : HelixClient.HUD.elements()) {
            boolean hovered = element.contains(mc, mouseX, mouseY);
            Gui.drawRect(element.getX() - 2, element.getY() - 2, element.getX() + element.getWidth(mc) + 2, element.getY() + element.getHeight(mc) + 2, hovered ? 0x6635D8FF : 0x33101822);
            element.render(mc);
        }
        fontRendererObj.drawString("HUD EDITOR", 12, 12, RenderUtil.CYAN);
        fontRendererObj.drawString("Drag elements | Mouse wheel scales | H/Esc saves", 12, 24, RenderUtil.WHITE);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (HudElement element : HelixClient.HUD.elements()) {
            if (element.contains(mc, mouseX, mouseY)) {
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
}
