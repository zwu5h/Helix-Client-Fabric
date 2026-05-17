package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.NumberSetting;
import net.minecraft.client.Minecraft;

public final class FullbrightModule extends Module {
    private final NumberSetting gamma = (NumberSetting) addSetting(new NumberSetting("Gamma", 12.0D, 1.0D, 16.0D, 0.5D));
    private Float previousGamma;

    public FullbrightModule() {
        super("Fullbright", "Raises client gamma for clearer visibility.", Category.VISUAL);
    }

    @Override
    public void onDisable() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (previousGamma != null) {
            minecraft.gameSettings.gammaSetting = previousGamma.floatValue();
            previousGamma = null;
        }
    }

    @Override
    public void tick(Minecraft minecraft) {
        if (previousGamma == null) {
            previousGamma = minecraft.gameSettings.gammaSetting;
        }
        minecraft.gameSettings.gammaSetting = gamma.getValue().floatValue();
    }
}
