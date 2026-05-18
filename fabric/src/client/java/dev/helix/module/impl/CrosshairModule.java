package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.BooleanSetting;
import dev.helix.setting.NumberSetting;

public final class CrosshairModule extends Module {
    public CrosshairModule() {
        super("Crosshair Editor", "Enables the Helix crosshair profile.", Category.HUD);
        addSetting(new NumberSetting("Size", 6.0, 2.0, 16.0, 1.0));
        addSetting(new NumberSetting("Gap", 4.0, 0.0, 12.0, 1.0));
        addSetting(new NumberSetting("Thickness", 1.0, 1.0, 5.0, 1.0));
        addSetting(new BooleanSetting("Dot", false));
        addSetting(new BooleanSetting("Rainbow", false));
    }
}
