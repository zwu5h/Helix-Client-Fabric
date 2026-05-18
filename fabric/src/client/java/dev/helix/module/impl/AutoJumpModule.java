package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.BooleanSetting;
import net.minecraft.client.MinecraftClient;

public final class AutoJumpModule extends Module {
    private final BooleanSetting onlyForward = addSetting(new BooleanSetting("Only Forward", false));
    private boolean previousAutoJump;

    public AutoJumpModule() {
        super("Auto Jump", "Toggles Minecraft auto-jump while enabled.", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        previousAutoJump = client.options.getAutoJump().getValue();
        client.options.getAutoJump().setValue(true);
    }

    @Override
    public void onDisable() {
        MinecraftClient.getInstance().options.getAutoJump().setValue(previousAutoJump);
    }

    @Override
    public void tick(MinecraftClient client) {
        client.options.getAutoJump().setValue(!onlyForward.value() || client.options.forwardKey.isPressed());
    }
}
