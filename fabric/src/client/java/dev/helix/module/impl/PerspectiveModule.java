package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import net.minecraft.client.MinecraftClient;

public final class PerspectiveModule extends Module {
    private int previousPerspective = -1;

    public PerspectiveModule() {
        super("Perspective", "Locks third-person perspective while enabled.", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        previousPerspective = client.options.getPerspective().ordinal();
        client.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
    }

    @Override
    public void onDisable() {
        if (previousPerspective >= 0) {
            MinecraftClient.getInstance().options.setPerspective(net.minecraft.client.option.Perspective.values()[previousPerspective]);
            previousPerspective = -1;
        }
    }
}
