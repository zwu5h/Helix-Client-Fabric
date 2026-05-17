package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;

public final class BetterChatModule extends Module {
    public BetterChatModule() {
        super("Better Chat", "Keeps chat styling lightweight and compatible.", Category.RENDER);
        setEnabled(true);
    }
}
