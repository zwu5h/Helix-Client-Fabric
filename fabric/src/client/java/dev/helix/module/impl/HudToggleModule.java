package dev.helix.module.impl;

import dev.helix.HelixClient;
import dev.helix.hud.HudElement;
import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.BooleanSetting;
import dev.helix.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import dev.helix.ui.HudEditorScreen;

public final class HudToggleModule extends Module {
    private static final int[] ACCENTS = {0xFF8A35FF, 0xFF35D8FF, 0xFFFF4FD8, 0xFF6CFF91, 0xFFFFD166};
    private static final int[] TEXT_COLORS = {0xFFEAF6FF, 0xFF35D8FF, 0xFFFF4FD8, 0xFF6CFF91, 0xFFFFD166};

    private final String elementId;
    private final int defaultX;
    private final int defaultY;
    private final BooleanSetting background = addSetting(new BooleanSetting("Background", true));
    private final NumberSetting backgroundAlpha = addSetting(new NumberSetting("Background Alpha", 170.0, 0.0, 255.0, 5.0));
    private final BooleanSetting border = addSetting(new BooleanSetting("Border", true));
    private final BooleanSetting rainbow = addSetting(new BooleanSetting("Rainbow", false));
    private final NumberSetting scale = addSetting(new NumberSetting("Scale", 1.0, 0.5, 3.0, 0.1));
    private final NumberSetting x = addSetting(new NumberSetting("X Position", 8.0, 0.0, 4096.0, 1.0));
    private final NumberSetting y = addSetting(new NumberSetting("Y Position", 8.0, 0.0, 4096.0, 1.0));
    private final NumberSetting accentPreset = addSetting(new NumberSetting("Accent Color", 0.0, 0.0, ACCENTS.length - 1.0, 1.0));
    private final NumberSetting textPreset = addSetting(new NumberSetting("Text Color", 0.0, 0.0, TEXT_COLORS.length - 1.0, 1.0));
    private final BooleanSetting resetDefaults = addSetting(new BooleanSetting("Reset Defaults", false));

    public HudToggleModule(String name, String description, String elementId) {
        super(name, description, Category.HUD);
        this.elementId = elementId;
        HudElement element = element();
        defaultX = element.x();
        defaultY = element.y();
        syncFromElement();
        setEnabled(true);
    }

    @Override
    public void onEnable() {
        element().setVisible(true);
        applySettings();
    }

    @Override
    public void onDisable() {
        element().setVisible(false);
    }

    @Override
    public void tick(MinecraftClient client) {
        if (client.currentScreen instanceof HudEditorScreen) {
            syncFromElement();
        } else if (enabled()) {
            applySettings();
        }
    }

    private void applySettings() {
        HudElement element = element();
        if (resetDefaults.value()) {
            element.setPosition(defaultX, defaultY);
            element.setScale(1.0D);
            element.setBackground(true);
            element.setBackgroundAlpha(170);
            element.setBorder(true);
            element.setRainbow(false);
            element.setAccentColor(ACCENTS[0]);
            element.setTextColor(TEXT_COLORS[0]);
            resetDefaults.setValue(false);
            syncFromElement();
            return;
        }
        element.setVisible(true);
        element.setBackground(background.value());
        element.setBackgroundAlpha(backgroundAlpha.value().intValue());
        element.setBorder(border.value());
        element.setRainbow(rainbow.value());
        element.setScale(scale.value());
        element.setPosition(x.value().intValue(), y.value().intValue());
        element.setAccentColor(ACCENTS[accentPreset.value().intValue()]);
        element.setTextColor(TEXT_COLORS[textPreset.value().intValue()]);
    }

    public void syncFromElement() {
        HudElement element = element();
        background.setValue(element.background());
        backgroundAlpha.setValue((double) element.backgroundAlpha());
        border.setValue(element.border());
        rainbow.setValue(element.rainbow());
        scale.setValue(element.scale());
        x.setValue((double) element.x());
        y.setValue((double) element.y());
        accentPreset.setValue((double) closestPreset(element.accentColor(), ACCENTS));
        textPreset.setValue((double) closestPreset(element.textColor(), TEXT_COLORS));
    }

    private int closestPreset(int color, int[] colors) {
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == color) {
                return i;
            }
        }
        return 0;
    }

    private HudElement element() {
        for (HudElement element : HelixClient.HUD.elements()) {
            if (element.id().equals(elementId)) {
                return element;
            }
        }
        throw new IllegalStateException("Missing HUD element: " + elementId);
    }
}
