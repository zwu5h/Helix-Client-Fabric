package dev.helix.module;

import dev.helix.module.impl.FullbrightModule;
import dev.helix.module.impl.BetterChatModule;
import dev.helix.module.impl.CrosshairModule;
import dev.helix.module.impl.HitColorModule;
import dev.helix.module.impl.MotionBlurModule;
import dev.helix.module.impl.NotificationModule;
import dev.helix.module.impl.PerspectiveModule;
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
        register(new ZoomModule());
        register(new FullbrightModule());
        register(new PerspectiveModule());
        register(new MotionBlurModule());
        register(new BetterChatModule());
        register(new HitColorModule());
        register(new CrosshairModule());
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
