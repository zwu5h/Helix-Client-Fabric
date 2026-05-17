package dev.helix.module;

import dev.helix.module.impl.FullbrightModule;
import dev.helix.module.impl.ToggleSprintModule;
import dev.helix.module.impl.ZoomModule;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleManager {
    private final List<Module> modules = new ArrayList<Module>();

    public void bootstrap() {
        if (!modules.isEmpty()) {
            return;
        }

        register(new ToggleSprintModule());
        register(new ZoomModule());
        register(new FullbrightModule());
    }

    public void register(Module module) {
        modules.add(module);
    }

    public void tick(Minecraft minecraft) {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.tick(minecraft);
            }
        }
    }

    public void onKeyInput() {
        for (Module module : modules) {
            if (module instanceof KeyInputAware) {
                ((KeyInputAware) module).onKeyInput();
            }
        }
    }

    public Module find(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public String names() {
        StringBuilder builder = new StringBuilder();
        for (Module module : modules) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(module.getName());
        }
        return builder.toString();
    }

    public List<Module> all() {
        return Collections.unmodifiableList(modules);
    }
}
