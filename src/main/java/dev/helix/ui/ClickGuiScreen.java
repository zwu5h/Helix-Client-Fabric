package dev.helix.ui;

import dev.helix.HelixClient;
import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public final class ClickGuiScreen extends GuiScreen {
    private Category selected = Category.MOVEMENT;
    private String search = "";
    private int scroll;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int left = width / 2 - 190;
        int top = height / 2 - 120;
        Gui.drawRect(left, top, left + 380, top + 240, 0xEE070A0F);
        Gui.drawRect(left, top, left + 380, top + 1, RenderUtil.CYAN);
        fontRendererObj.drawString("HELIX 1.8.9", left + 16, top + 14, RenderUtil.WHITE);
        fontRendererObj.drawString("Search: " + (search.length() == 0 ? "all" : search), left + 112, top + 14, RenderUtil.MUTED);

        int categoryY = top + 42;
        for (Category category : Category.values()) {
            boolean active = category == selected;
            Gui.drawRect(left + 12, categoryY, left + 96, categoryY + 22, active ? 0xCC123140 : 0x66202A34);
            fontRendererObj.drawString(pretty(category.name()), left + 22, categoryY + 7, active ? RenderUtil.CYAN : RenderUtil.WHITE);
            categoryY += 28;
        }

        int moduleX = left + 112;
        int moduleY = top + 42 - scroll;
        for (Module module : HelixClient.MODULES.all()) {
            if (module.getCategory() != selected || !matchesSearch(module)) {
                continue;
            }

            boolean hovered = inside(mouseX, mouseY, moduleX, moduleY, 248, 38);
            Gui.drawRect(moduleX, moduleY, moduleX + 248, moduleY + 38, hovered ? 0xCC102532 : 0x99101720);
            Gui.drawRect(moduleX, moduleY, moduleX + 3, moduleY + 38, module.isEnabled() ? RenderUtil.CYAN : 0xFF37404A);
            fontRendererObj.drawString(module.getName(), moduleX + 12, moduleY + 8, RenderUtil.WHITE);
            fontRendererObj.drawString(module.getDescription(), moduleX + 12, moduleY + 22, RenderUtil.MUTED);
            fontRendererObj.drawString(module.isEnabled() ? "ON" : "OFF", moduleX + 214, moduleY + 14, module.isEnabled() ? RenderUtil.CYAN : RenderUtil.MUTED);
            moduleY += 46;
        }

        fontRendererObj.drawString("Right Shift closes | type to search | backspace clears", left + 16, top + 222, RenderUtil.MUTED);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int left = width / 2 - 190;
        int top = height / 2 - 120;

        int categoryY = top + 42;
        for (Category category : Category.values()) {
            if (inside(mouseX, mouseY, left + 12, categoryY, 84, 22)) {
                selected = category;
                scroll = 0;
                return;
            }
            categoryY += 28;
        }

        int moduleX = left + 112;
        int moduleY = top + 42 - scroll;
        for (Module module : HelixClient.MODULES.all()) {
            if (module.getCategory() != selected || !matchesSearch(module)) {
                continue;
            }

            if (inside(mouseX, mouseY, moduleX, moduleY, 248, 38)) {
                module.toggle();
                HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
                return;
            }
            moduleY += 46;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int delta = org.lwjgl.input.Mouse.getEventDWheel();
        if (delta != 0) {
            scroll = Math.max(0, scroll - (delta > 0 ? 18 : -18));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_BACK && search.length() > 0) {
            search = search.substring(0, search.length() - 1);
            return;
        }

        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RSHIFT) {
            mc.displayGuiScreen(null);
            return;
        }

        if (Character.isLetterOrDigit(typedChar) || Character.isSpaceChar(typedChar)) {
            search += typedChar;
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private boolean matchesSearch(Module module) {
        return search.length() == 0 || module.getName().toLowerCase().contains(search.toLowerCase());
    }

    private boolean inside(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private String pretty(String value) {
        return value.charAt(0) + value.substring(1).toLowerCase();
    }
}
