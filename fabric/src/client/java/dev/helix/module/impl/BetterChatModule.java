package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.BooleanSetting;

public final class BetterChatModule extends Module {
    public BetterChatModule() {
        super("Better Chat", "Keeps chat styling lightweight and compatible.", Category.RENDER);
        addSetting(new BooleanSetting("Timestamps", false));
        addSetting(new BooleanSetting("Clean Background", true));
        addSetting(new BooleanSetting("Compact Lines", false));
        setEnabled(true);
    }
}
