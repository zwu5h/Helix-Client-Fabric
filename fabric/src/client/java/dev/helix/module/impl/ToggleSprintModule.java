package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.BooleanSetting;
import net.minecraft.client.MinecraftClient;

public final class ToggleSprintModule extends Module {
    private final BooleanSetting stopWhileSneaking = addSetting(new BooleanSetting("Stop While Sneaking", true));
    private final BooleanSetting requireFood = addSetting(new BooleanSetting("Require Food", true));

    public ToggleSprintModule() {
        super("Toggle Sprint", "Keeps sprint active while moving forward.", Category.MOVEMENT);
    }

    @Override
    public void tick(MinecraftClient client) {
        if (client.player == null || stopWhileSneaking.value() && client.options.sneakKey.isPressed()) {
            return;
        }

        if (client.options.forwardKey.isPressed() && (!requireFood.value() || client.player.getHungerManager().getFoodLevel() > 6)) {
            client.player.setSprinting(true);
        }
    }
}
