package dev.helix.module.impl;

import dev.helix.HelixClient;
import dev.helix.hud.HudElement;
import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.BooleanSetting;
import dev.helix.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;

public final class HudToggleModule extends Module {
    private final String elementId;
    private final BooleanSetting background = addSetting(new BooleanSetting("Background", true));
    private final BooleanSetting rainbow = addSetting(new BooleanSetting("Rainbow", false));
    private final NumberSetting scale = addSetting(new NumberSetting("Scale", 1.0, 0.5, 3.0, 0.1));

    public HudToggleModule(String name, String description, String elementId) {
        super(name, description, Category.HUD);
        this.elementId = elementId;
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
        if (enabled()) {
            applySettings();
        }
    }

    private void applySettings() {
        HudElement element = element();
        element.setVisible(true);
        element.setBackground(background.value());
        element.setRainbow(rainbow.value());
        element.setScale(scale.value());
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
