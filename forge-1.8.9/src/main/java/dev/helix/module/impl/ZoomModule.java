package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public final class ZoomModule extends Module {
    private final NumberSetting zoomFov = (NumberSetting) addSetting(new NumberSetting("FOV", 30.0D, 10.0D, 70.0D, 1.0D));
    private final KeyBinding key = new KeyBinding("key.helixclient.zoom", Keyboard.KEY_C, "key.categories.helixclient");
    private Float previousFov;

    public ZoomModule() {
        super("Zoom", "Temporarily narrows the field of view while holding C.", Category.RENDER);
        ClientRegistry.registerKeyBinding(key);
        setEnabled(true);
    }

    @Override
    public void tick(Minecraft minecraft) {
        if (key.isKeyDown()) {
            if (previousFov == null) {
                previousFov = minecraft.gameSettings.fovSetting;
            }
            minecraft.gameSettings.fovSetting = zoomFov.getValue().floatValue();
            return;
        }

        if (previousFov != null) {
            minecraft.gameSettings.fovSetting = previousFov.floatValue();
            previousFov = null;
        }
    }
}
