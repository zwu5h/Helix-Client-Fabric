package dev.helix.ui;

import dev.helix.HelixClient;
import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.render.RenderUtil;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;

import java.util.Locale;

public final class ClickGuiScreen extends Screen {
    private Category selected = Category.MOVEMENT;
    private String search = "";
    private int scroll;

    public ClickGuiScreen() {
        super(Text.literal("Helix"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        int left = width / 2 - 190;
        int top = height / 2 - 120;
        context.fill(left, top, left + 380, top + 240, 0xEE070A0F);
        context.fill(left, top, left + 380, top + 1, RenderUtil.CYAN);
        context.drawText(textRenderer, "HELIX", left + 16, top + 14, RenderUtil.WHITE, false);
        context.drawText(textRenderer, "Search: " + (search.isEmpty() ? "all" : search), left + 112, top + 14, RenderUtil.MUTED, false);

        int categoryY = top + 42;
        for (Category category : Category.values()) {
            boolean active = category == selected;
            int color = active ? 0xCC123140 : 0x66202A34;
            context.fill(left + 12, categoryY, left + 96, categoryY + 22, color);
            context.drawText(textRenderer, pretty(category.name()), left + 22, categoryY + 7, active ? RenderUtil.CYAN : RenderUtil.WHITE, false);
            categoryY += 28;
        }

        int moduleX = left + 112;
        int moduleY = top + 42 - scroll;
        for (Module module : HelixClient.MODULES.all()) {
            if (module.category() != selected || !matchesSearch(module)) {
                continue;
            }

            boolean hovered = mouseX >= moduleX && mouseX <= moduleX + 248 && mouseY >= moduleY && mouseY <= moduleY + 38;
            context.fill(moduleX, moduleY, moduleX + 248, moduleY + 38, hovered ? 0xCC102532 : 0x99101720);
            context.fill(moduleX, moduleY, moduleX + 3, moduleY + 38, module.enabled() ? RenderUtil.CYAN : 0xFF37404A);
            context.drawText(textRenderer, module.name(), moduleX + 12, moduleY + 8, RenderUtil.WHITE, false);
            context.drawText(textRenderer, module.description(), moduleX + 12, moduleY + 22, RenderUtil.MUTED, false);
            context.drawText(textRenderer, module.enabled() ? "ON" : "OFF", moduleX + 214, moduleY + 14, module.enabled() ? RenderUtil.CYAN : RenderUtil.MUTED, false);
            moduleY += 46;
        }

        context.drawText(textRenderer, "Right Shift closes | type to search | backspace clears", left + 16, top + 222, RenderUtil.MUTED, false);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        double mouseX = click.x();
        double mouseY = click.y();
        int left = width / 2 - 190;
        int top = height / 2 - 120;

        int categoryY = top + 42;
        for (Category category : Category.values()) {
            if (inside(mouseX, mouseY, left + 12, categoryY, 84, 22)) {
                selected = category;
                scroll = 0;
                return true;
            }
            categoryY += 28;
        }

        int moduleX = left + 112;
        int moduleY = top + 42 - scroll;
        for (Module module : HelixClient.MODULES.all()) {
            if (module.category() != selected || !matchesSearch(module)) {
                continue;
            }

            if (inside(mouseX, mouseY, moduleX, moduleY, 248, 38)) {
                module.toggle();
                HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
                return true;
            }
            moduleY += 46;
        }

        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scroll = Math.max(0, scroll - (int) (verticalAmount * 18));
        return true;
    }

    @Override
    public boolean charTyped(CharInput input) {
        String value = input.asString();
        if (input.isValidChar() && !value.isEmpty() && (Character.isLetterOrDigit(value.codePointAt(0)) || Character.isSpaceChar(value.codePointAt(0)))) {
            search += value;
            return true;
        }
        return super.charTyped(input);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == 259 && !search.isEmpty()) {
            search = search.substring(0, search.length() - 1);
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private boolean matchesSearch(Module module) {
        return search.isBlank() || module.name().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT));
    }

    private boolean inside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private String pretty(String value) {
        return value.charAt(0) + value.substring(1).toLowerCase(Locale.ROOT);
    }
}
