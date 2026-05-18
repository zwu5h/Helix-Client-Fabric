package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.NumberSetting;

public final class HitColorModule extends Module {
    public HitColorModule() {
        super("Hit Color", "Central toggle for future damage tint customization.", Category.VISUAL);
        addSetting(new NumberSetting("Red", 255.0, 0.0, 255.0, 1.0));
        addSetting(new NumberSetting("Green", 64.0, 0.0, 255.0, 1.0));
        addSetting(new NumberSetting("Blue", 120.0, 0.0, 255.0, 1.0));
        addSetting(new NumberSetting("Alpha", 145.0, 0.0, 255.0, 1.0));
    }
}
