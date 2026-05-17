package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import net.minecraft.client.MinecraftClient;

public final class ToggleSprintModule extends Module {
    public ToggleSprintModule() {
        super("Toggle Sprint", "Keeps sprint active while moving forward.", Category.MOVEMENT);
    }

    @Override
    public void tick(MinecraftClient client) {
        if (client.player == null || client.options.sneakKey.isPressed()) {
            return;
        }

        if (client.options.forwardKey.isPressed() && client.player.getHungerManager().getFoodLevel() > 6) {
            client.player.setSprinting(true);
        }
    }
}
