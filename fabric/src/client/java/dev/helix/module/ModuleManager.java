package dev.helix.module;

import dev.helix.module.impl.FullbrightModule;
import dev.helix.module.impl.BetterChatModule;
import dev.helix.module.impl.AutoJumpModule;
import dev.helix.module.impl.CrosshairModule;
import dev.helix.module.impl.HitColorModule;
import dev.helix.module.impl.HudToggleModule;
import dev.helix.module.impl.MotionBlurModule;
import dev.helix.module.impl.NotificationModule;
import dev.helix.module.impl.PerspectiveModule;
import dev.helix.module.impl.PerformanceModule;
import dev.helix.module.impl.ScreenshotManagerModule;
import dev.helix.module.impl.ToggleSprintModule;
import dev.helix.module.impl.ZoomModule;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void bootstrap() {
        if (!modules.isEmpty()) {
            return;
        }

        register(new ToggleSprintModule());
        register(new AutoJumpModule());
        register(new ZoomModule());
        register(new FullbrightModule());
        register(new PerspectiveModule());
        register(new MotionBlurModule());
        register(new PerformanceModule());
        register(new BetterChatModule());
        register(new HitColorModule());
        register(new CrosshairModule());
        register(new HudToggleModule("Armor Status", "Shows equipped armor durability on the HUD.", "armor"));
        register(new HudToggleModule("Potion Effects", "Shows active potion effects on the HUD.", "potions"));
        register(new HudToggleModule("Coordinates", "Shows the current block position.", "coordinates"));
        register(new HudToggleModule("Keystrokes", "Shows movement key input on the HUD.", "keystrokes"));
        register(new HudToggleModule("FPS Counter", "Shows frames per second on the HUD.", "fps"));
        register(new HudToggleModule("CPS Counter", "Shows click speed on the HUD.", "cps"));
        register(new HudToggleModule("Ping Display", "Shows current server latency.", "ping"));
        register(new HudToggleModule("Direction", "Shows the direction you are facing.", "direction"));
        register(new HudToggleModule("Speed Meter", "Shows movement speed on the HUD.", "speed"));
        register(new HudToggleModule("Memory Usage", "Shows Java memory use on the HUD.", "memory"));
        register(new HudToggleModule("Clock", "Shows local time on the HUD.", "clock"));
        register(new HudToggleModule("Session Info", "Shows session play time on the HUD.", "session"));
        register(new HudToggleModule("Active Modules", "Shows enabled modules on the HUD.", "active_modules"));
        register(new ScreenshotManagerModule());
        register(new NotificationModule());
    }

    public void register(Module module) {
        modules.add(module);
    }

    public void tick(MinecraftClient client) {
        for (Module module : modules) {
            if (module.enabled()) {
                module.tick(client);
            }
        }
    }

    public void syncHudSettingsFromElements() {
        for (Module module : modules) {
            if (module instanceof HudToggleModule hudToggleModule) {
                hudToggleModule.syncFromElement();
            }
        }
    }

    public Optional<Module> find(String name) {
        String normalized = name.toLowerCase(Locale.ROOT);
        return modules.stream()
                .filter(module -> module.name().toLowerCase(Locale.ROOT).equals(normalized))
                .findFirst();
    }

    public List<Module> all() {
        return Collections.unmodifiableList(modules);
    }
}
