package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import net.minecraft.client.Minecraft;

public final class PerspectiveModule extends Module {
    private int previousPerspective = -1;

    public PerspectiveModule() {
        super("Perspective", "Locks third-person perspective while enabled.", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        previousPerspective = Minecraft.getMinecraft().gameSettings.thirdPersonView;
        Minecraft.getMinecraft().gameSettings.thirdPersonView = 1;
    }

    @Override
    public void onDisable() {
        if (previousPerspective >= 0) {
            Minecraft.getMinecraft().gameSettings.thirdPersonView = previousPerspective;
            previousPerspective = -1;
        }
    }
}
