package dev.helix.ui;

import dev.helix.HelixClient;
import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.render.RenderUtil;
import dev.helix.setting.BooleanSetting;
import dev.helix.setting.NumberSetting;
import dev.helix.setting.Setting;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class ClickGuiScreen extends Screen {
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 316;
    private static final int HEADER_HEIGHT = 38;
    private static final int CATEGORY_WIDTH = 118;
    private static final int MODULE_WIDTH = 344;
    private static final int MODULE_BASE_HEIGHT = 42;
    private static final int SETTING_HEIGHT = 26;

    private final Set<String> expanded = new HashSet<>();
    private Category selected = Category.MOVEMENT;
    private String search = "";
    private int scroll;
    private NumberSetting draggingSlider;
    private int draggingSliderX;
    private int draggingSliderWidth;

    public ClickGuiScreen() {
        super(Text.literal("Helix"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        int left = left();
        int top = top();
        int contentTop = top + HEADER_HEIGHT + 12;
        int contentBottom = top + PANEL_HEIGHT - 24;

        clampScroll();
        context.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xF006090D);
        context.fill(left, top, left + PANEL_WIDTH, top + 1, RenderUtil.CYAN);
        context.fill(left, top + HEADER_HEIGHT, left + PANEL_WIDTH, top + HEADER_HEIGHT + 1, 0x33182734);
        context.drawText(textRenderer, "HELIX", left + 16, top + 14, RenderUtil.WHITE, false);
        context.drawText(textRenderer, "Search: " + (search.isEmpty() ? "all" : search), left + 100, top + 14, RenderUtil.MUTED, false);

        renderCategories(context, mouseX, mouseY, left, contentTop);
        renderModules(context, mouseX, mouseY, left + CATEGORY_WIDTH + 24, contentTop, contentBottom);

        int maxScroll = maxScroll();
        if (maxScroll > 0) {
            int trackX = left + PANEL_WIDTH - 14;
            int trackTop = contentTop;
            int trackHeight = contentBottom - contentTop;
            int thumbHeight = Math.max(28, trackHeight * trackHeight / (trackHeight + maxScroll));
            int thumbY = trackTop + (trackHeight - thumbHeight) * scroll / maxScroll;
            context.fill(trackX, trackTop, trackX + 3, contentBottom, 0x55202A34);
            context.fill(trackX, thumbY, trackX + 3, thumbY + thumbHeight, 0xAA35D8FF);
        }

        context.drawText(textRenderer, "Right Shift closes  |  type to search  |  click EDIT for settings", left + 16, top + PANEL_HEIGHT - 15, RenderUtil.MUTED, false);
    }

    private void renderCategories(DrawContext context, int mouseX, int mouseY, int left, int y) {
        for (Category category : Category.values()) {
            boolean active = category == selected;
            boolean hovered = inside(mouseX, mouseY, left + 12, y, CATEGORY_WIDTH - 20, 24);
            context.fill(left + 12, y, left + CATEGORY_WIDTH - 8, y + 24, active ? 0xCC123140 : hovered ? 0x88202A34 : 0x55202A34);
            context.fill(left + 12, y, left + 14, y + 24, active ? RenderUtil.CYAN : 0x5535D8FF);
            context.drawText(textRenderer, pretty(category.name()), left + 22, y + 8, active ? RenderUtil.CYAN : RenderUtil.WHITE, false);
            y += 31;
        }
    }

    private void renderModules(DrawContext context, int mouseX, int mouseY, int moduleX, int contentTop, int contentBottom) {
        int y = contentTop - scroll;
        context.enableScissor(moduleX, contentTop, moduleX + MODULE_WIDTH, contentBottom);
        for (Module module : HelixClient.MODULES.all()) {
            if (module.category() != selected || !matchesSearch(module)) {
                continue;
            }

            int rowHeight = rowHeight(module);
            if (y + rowHeight >= contentTop && y <= contentBottom) {
                renderModule(context, mouseX, mouseY, module, moduleX, y);
            }
            y += rowHeight + 8;
        }
        context.disableScissor();
    }

    private void renderModule(DrawContext context, int mouseX, int mouseY, Module module, int x, int y) {
        boolean hovered = inside(mouseX, mouseY, x, y, MODULE_WIDTH, MODULE_BASE_HEIGHT);
        boolean open = expanded.contains(module.name());
        int height = rowHeight(module);

        context.fill(x, y, x + MODULE_WIDTH, y + height, hovered ? 0xCC102532 : 0xB0101720);
        context.fill(x, y, x + 3, y + height, module.enabled() ? RenderUtil.CYAN : 0xFF37404A);
        context.drawText(textRenderer, module.name(), x + 12, y + 8, RenderUtil.WHITE, false);
        context.drawText(textRenderer, trim(module.description(), MODULE_WIDTH - 94), x + 12, y + 23, RenderUtil.MUTED, false);
        context.drawText(textRenderer, module.enabled() ? "ON" : "OFF", x + MODULE_WIDTH - 76, y + 9, module.enabled() ? RenderUtil.CYAN : RenderUtil.MUTED, false);
        if (!module.settings().isEmpty()) {
            context.fill(x + MODULE_WIDTH - 45, y + 8, x + MODULE_WIDTH - 12, y + 26, open ? 0xAA123140 : 0x7737404A);
            context.drawText(textRenderer, "EDIT", x + MODULE_WIDTH - 39, y + 13, open ? RenderUtil.CYAN : RenderUtil.WHITE, false);
        }

        if (open) {
            int settingY = y + MODULE_BASE_HEIGHT;
            for (Setting<?> setting : module.settings()) {
                renderSetting(context, setting, x + 12, settingY, MODULE_WIDTH - 24);
                settingY += SETTING_HEIGHT;
            }
        }
    }

    private void renderSetting(DrawContext context, Setting<?> setting, int x, int y, int width) {
        context.fill(x, y, x + width, y + 1, 0x33202A34);
        context.drawText(textRenderer, setting.name(), x, y + 9, RenderUtil.WHITE, false);

        if (setting instanceof BooleanSetting booleanSetting) {
            int bx = x + width - 44;
            context.fill(bx, y + 6, bx + 34, y + 20, booleanSetting.value() ? 0xAA123140 : 0x7737404A);
            context.drawText(textRenderer, booleanSetting.value() ? "ON" : "OFF", bx + 9, y + 10, booleanSetting.value() ? RenderUtil.CYAN : RenderUtil.MUTED, false);
        } else if (setting instanceof NumberSetting numberSetting) {
            int sliderX = x + width - 138;
            int sliderY = y + 13;
            int sliderWidth = 92;
            double progress = (numberSetting.value() - numberSetting.min()) / (numberSetting.max() - numberSetting.min());
            int fill = (int) (sliderWidth * progress);
            context.fill(sliderX, sliderY, sliderX + sliderWidth, sliderY + 3, 0x7737404A);
            context.fill(sliderX, sliderY, sliderX + fill, sliderY + 3, RenderUtil.CYAN);
            context.drawText(textRenderer, String.format(Locale.ROOT, "%.1f", numberSetting.value()), x + width - 36, y + 9, RenderUtil.MUTED, false);
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        double mouseX = click.x();
        double mouseY = click.y();
        int left = left();
        int top = top();
        int contentTop = top + HEADER_HEIGHT + 12;
        int contentBottom = top + PANEL_HEIGHT - 24;

        int categoryY = contentTop;
        for (Category category : Category.values()) {
            if (inside(mouseX, mouseY, left + 12, categoryY, CATEGORY_WIDTH - 20, 24)) {
                selected = category;
                scroll = 0;
                return true;
            }
            categoryY += 31;
        }

        if (mouseY < contentTop || mouseY > contentBottom) {
            return super.mouseClicked(click, doubled);
        }

        int moduleX = left + CATEGORY_WIDTH + 24;
        int moduleY = contentTop - scroll;
        for (Module module : HelixClient.MODULES.all()) {
            if (module.category() != selected || !matchesSearch(module)) {
                continue;
            }

            int rowHeight = rowHeight(module);
            if (inside(mouseX, mouseY, moduleX, moduleY, MODULE_WIDTH, rowHeight)) {
                return clickModule(module, mouseX, mouseY, moduleX, moduleY);
            }
            moduleY += rowHeight + 8;
        }

        return super.mouseClicked(click, doubled);
    }

    private boolean clickModule(Module module, double mouseX, double mouseY, int moduleX, int moduleY) {
        if (inside(mouseX, mouseY, moduleX + MODULE_WIDTH - 45, moduleY + 8, 33, 18) && !module.settings().isEmpty()) {
            if (!expanded.remove(module.name())) {
                expanded.add(module.name());
            }
            clampScroll();
            return true;
        }

        if (inside(mouseX, mouseY, moduleX, moduleY, MODULE_WIDTH, MODULE_BASE_HEIGHT)) {
            module.toggle();
            HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
            return true;
        }

        if (expanded.contains(module.name())) {
            int settingY = moduleY + MODULE_BASE_HEIGHT;
            for (Setting<?> setting : module.settings()) {
                if (inside(mouseX, mouseY, moduleX + 12, settingY, MODULE_WIDTH - 24, SETTING_HEIGHT)) {
                    clickSetting(setting, mouseX, moduleX + 12, MODULE_WIDTH - 24);
                    return true;
                }
                settingY += SETTING_HEIGHT;
            }
        }

        return true;
    }

    private void clickSetting(Setting<?> setting, double mouseX, int x, int width) {
        if (setting instanceof BooleanSetting booleanSetting) {
            booleanSetting.toggle();
        } else if (setting instanceof NumberSetting numberSetting) {
            draggingSlider = numberSetting;
            draggingSliderX = x + width - 138;
            draggingSliderWidth = 92;
            updateSlider(mouseX);
        }
        HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        if (draggingSlider != null) {
            updateSlider(click.x());
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (draggingSlider != null) {
            draggingSlider = null;
            HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
            return true;
        }
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int left = left();
        int top = top();
        if (!inside(mouseX, mouseY, left, top, PANEL_WIDTH, PANEL_HEIGHT)) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
        scroll = Math.max(0, Math.min(maxScroll(), scroll - (int) (verticalAmount * 24)));
        return true;
    }

    @Override
    public boolean charTyped(CharInput input) {
        String value = input.asString();
        if (input.isValidChar() && !value.isEmpty() && (Character.isLetterOrDigit(value.codePointAt(0)) || Character.isSpaceChar(value.codePointAt(0)))) {
            search += value;
            scroll = 0;
            return true;
        }
        return super.charTyped(input);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == 259 && !search.isEmpty()) {
            search = search.substring(0, search.length() - 1);
            scroll = 0;
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void updateSlider(double mouseX) {
        double progress = Math.max(0.0D, Math.min(1.0D, (mouseX - draggingSliderX) / draggingSliderWidth));
        double raw = draggingSlider.min() + (draggingSlider.max() - draggingSlider.min()) * progress;
        double stepped = Math.round(raw / draggingSlider.step()) * draggingSlider.step();
        draggingSlider.setValue(stepped);
    }

    private int rowHeight(Module module) {
        if (!expanded.contains(module.name())) {
            return MODULE_BASE_HEIGHT;
        }
        return MODULE_BASE_HEIGHT + module.settings().size() * SETTING_HEIGHT;
    }

    private int maxScroll() {
        int contentHeight = 0;
        for (Module module : HelixClient.MODULES.all()) {
            if (module.category() == selected && matchesSearch(module)) {
                contentHeight += rowHeight(module) + 8;
            }
        }
        int viewportHeight = PANEL_HEIGHT - HEADER_HEIGHT - 36;
        return Math.max(0, contentHeight - viewportHeight);
    }

    private void clampScroll() {
        scroll = Math.max(0, Math.min(scroll, maxScroll()));
    }

    private boolean matchesSearch(Module module) {
        return search.isBlank()
                || module.name().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))
                || module.description().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT));
    }

    private boolean inside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private int left() {
        return width / 2 - PANEL_WIDTH / 2;
    }

    private int top() {
        return height / 2 - PANEL_HEIGHT / 2;
    }

    private String pretty(String value) {
        return value.charAt(0) + value.substring(1).toLowerCase(Locale.ROOT);
    }

    private String trim(String value, int maxWidth) {
        if (textRenderer.getWidth(value) <= maxWidth) {
            return value;
        }
        String suffix = "...";
        int width = Math.max(0, maxWidth - textRenderer.getWidth(suffix));
        String result = value;
        while (!result.isEmpty() && textRenderer.getWidth(result) > width) {
            result = result.substring(0, result.length() - 1);
        }
        return result + suffix;
    }
}
