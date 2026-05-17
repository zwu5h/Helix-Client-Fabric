package dev.helix.module.impl;

import dev.helix.HelixClient;
import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.NumberSetting;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class ZoomModule extends Module {
    private final NumberSetting zoomFov = addSetting(new NumberSetting("FOV", 30.0, 10.0, 70.0, 1.0));
    private final KeyBinding key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.helix-client.zoom",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            HelixClient.KEY_CATEGORY
    ));
    private Integer previousFov;

    public ZoomModule() {
        super("Zoom", "Temporarily narrows the field of view while holding C.", Category.RENDER);
        setEnabled(true);
    }

    @Override
    public void tick(MinecraftClient client) {
        if (key.isPressed()) {
            if (previousFov == null) {
                previousFov = client.options.getFov().getValue();
            }
            client.options.getFov().setValue(zoomFov.value().intValue());
            return;
        }

        if (previousFov != null) {
            client.options.getFov().setValue(previousFov);
            previousFov = null;
        }
    }
}
