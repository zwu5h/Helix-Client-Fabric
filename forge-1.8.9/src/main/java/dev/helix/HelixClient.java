package dev.helix;

import dev.helix.config.ConfigManager;
import dev.helix.hud.HudManager;
import dev.helix.module.ModuleManager;
import dev.helix.ui.ClickGuiScreen;
import dev.helix.ui.HelixMainMenuScreen;
import dev.helix.ui.HudEditorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

@Mod(
        modid = HelixClient.MOD_ID,
        name = HelixClient.NAME,
        version = HelixClient.VERSION,
        acceptedMinecraftVersions = "[1.8.9]",
        clientSideOnly = true
)
public final class HelixClient {
    public static final String MOD_ID = "helixclient";
    public static final String NAME = "Helix Client";
    public static final String VERSION = "0.1.0";

    public static final ModuleManager MODULES = new ModuleManager();
    public static final HudManager HUD = new HudManager();
    public static final ConfigManager CONFIG = new ConfigManager();

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private KeyBinding clickGuiKey;
    private KeyBinding hudEditorKey;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CONFIG.load();
        MODULES.bootstrap();
        HUD.bootstrap();
        CONFIG.apply(MODULES, HUD);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        clickGuiKey = new KeyBinding("key.helixclient.click_gui", Keyboard.KEY_RSHIFT, "key.categories.helixclient");
        hudEditorKey = new KeyBinding("key.helixclient.hud_editor", Keyboard.KEY_H, "key.categories.helixclient");
        ClientRegistry.registerKeyBinding(clickGuiKey);
        ClientRegistry.registerKeyBinding(hudEditorKey);

        MinecraftForge.EVENT_BUS.register(HUD);
        MinecraftForge.EVENT_BUS.register(this);
        net.minecraftforge.fml.common.FMLCommonHandler.instance().bus().register(this);
        ClientCommandHandler.instance.registerCommand(new dev.helix.command.HelixCommand());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || minecraft.thePlayer == null) {
            return;
        }

        while (clickGuiKey.isPressed()) {
            minecraft.displayGuiScreen(new ClickGuiScreen());
        }
        while (hudEditorKey.isPressed()) {
            minecraft.displayGuiScreen(new HudEditorScreen());
        }

        MODULES.tick(minecraft);
        HUD.tick(minecraft);
        CONFIG.autoSave(MODULES, HUD);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        MODULES.onKeyInput();
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiMainMenu && !(event.gui instanceof HelixMainMenuScreen)) {
            event.gui = new HelixMainMenuScreen();
        }
    }
}
