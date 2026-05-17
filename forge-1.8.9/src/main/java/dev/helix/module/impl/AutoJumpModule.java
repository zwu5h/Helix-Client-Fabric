package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import net.minecraft.client.Minecraft;

public final class AutoJumpModule extends Module {
    public AutoJumpModule() {
        super("Auto Jump", "Automatically jumps while walking into blocks.", Category.MOVEMENT);
    }

    @Override
    public void tick(Minecraft minecraft) {
        if (minecraft.thePlayer == null || minecraft.theWorld == null) {
            return;
        }
        if (minecraft.gameSettings.keyBindForward.isKeyDown() && minecraft.thePlayer.onGround && minecraft.thePlayer.isCollidedHorizontally) {
            minecraft.thePlayer.jump();
        }
    }
}
