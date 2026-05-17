package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import net.minecraft.client.Minecraft;

public final class ToggleSprintModule extends Module {
    public ToggleSprintModule() {
        super("Toggle Sprint", "Keeps sprint active while moving forward.", Category.MOVEMENT);
    }

    @Override
    public void tick(Minecraft minecraft) {
        if (minecraft.thePlayer == null || minecraft.gameSettings.keyBindSneak.isKeyDown()) {
            return;
        }

        if (minecraft.gameSettings.keyBindForward.isKeyDown() && minecraft.thePlayer.getFoodStats().getFoodLevel() > 6) {
            minecraft.thePlayer.setSprinting(true);
        }
    }
}
