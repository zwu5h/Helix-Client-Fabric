package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;

public final class FullbrightModule extends Module {
    private final NumberSetting gamma = addSetting(new NumberSetting("Gamma", 12.0, 1.0, 16.0, 0.5));
    private Double previousGamma;

    public FullbrightModule() {
        super("Fullbright", "Raises client gamma for clearer visibility.", Category.VISUAL);
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (previousGamma != null) {
            client.options.getGamma().setValue(previousGamma);
            previousGamma = null;
        }
    }

    @Override
    public void tick(MinecraftClient client) {
        if (previousGamma == null) {
            previousGamma = client.options.getGamma().getValue();
        }
        client.options.getGamma().setValue(gamma.value());
    }
}
