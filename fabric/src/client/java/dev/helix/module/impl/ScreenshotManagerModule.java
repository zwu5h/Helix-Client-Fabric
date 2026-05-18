package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.BooleanSetting;
import dev.helix.setting.NumberSetting;

public final class ScreenshotManagerModule extends Module {
    public ScreenshotManagerModule() {
        super("Screenshot Manager", "Keeps screenshot workflow grouped in Helix.", Category.MISC);
        addSetting(new BooleanSetting("Copy Name", true));
        addSetting(new BooleanSetting("Chat Toast", true));
        addSetting(new NumberSetting("Preview Time", 2.0, 0.0, 6.0, 0.5));
    }
}
