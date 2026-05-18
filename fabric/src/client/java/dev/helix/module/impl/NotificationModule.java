package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.BooleanSetting;
import dev.helix.setting.NumberSetting;

public final class NotificationModule extends Module {
    public NotificationModule() {
        super("Notifications", "Shows client status through the Helix HUD layer.", Category.MISC);
        addSetting(new NumberSetting("Duration", 3.0, 1.0, 8.0, 0.5));
        addSetting(new BooleanSetting("Compact", true));
        addSetting(new BooleanSetting("Sound", false));
        setEnabled(true);
    }
}
