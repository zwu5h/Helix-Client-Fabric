package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;

public final class NotificationModule extends Module {
    public NotificationModule() {
        super("Notifications", "Shows client status through the Helix HUD layer.", Category.MISC);
        setEnabled(true);
    }
}
