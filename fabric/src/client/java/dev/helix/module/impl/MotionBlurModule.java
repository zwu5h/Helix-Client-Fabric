package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.NumberSetting;

public final class MotionBlurModule extends Module {
    public MotionBlurModule() {
        super("Motion Blur", "Reserved toggle for shader-pack motion blur compatibility.", Category.VISUAL);
        addSetting(new NumberSetting("Strength", 45.0, 0.0, 100.0, 5.0));
        addSetting(new NumberSetting("Samples", 6.0, 2.0, 12.0, 1.0));
    }
}
