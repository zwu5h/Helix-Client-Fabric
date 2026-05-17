package dev.helix;

import dev.helix.config.ConfigManager;
import dev.helix.event.EventBus;
import dev.helix.hud.HudManager;
import dev.helix.module.ModuleManager;
import dev.helix.ui.ClickGuiScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class HelixClient implements ClientModInitializer {
    public static final String MOD_ID = "helix-client";
    public static final KeyBinding.Category KEY_CATEGORY = KeyBinding.Category.create(Identifier.of(MOD_ID, "controls"));
    public static final EventBus EVENTS = new EventBus();
    public static final ModuleManager MODULES = new ModuleManager();
    public static final HudManager HUD = new HudManager();
    public static final ConfigManager CONFIG = new ConfigManager();

    private KeyBinding clickGuiKey;

    @Override
    public void onInitializeClient() {
        CONFIG.load();
        MODULES.bootstrap();
        HUD.bootstrap();
        CONFIG.apply(MODULES, HUD);

        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.helix-client.click_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KEY_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> HUD.render(drawContext, tickCounter));
    }

    private void tick(MinecraftClient client) {
        while (clickGuiKey.wasPressed()) {
            client.setScreen(new ClickGuiScreen());
        }

        MODULES.tick(client);
        HUD.tick(client);
        CONFIG.autoSave(MODULES, HUD);
    }
}
