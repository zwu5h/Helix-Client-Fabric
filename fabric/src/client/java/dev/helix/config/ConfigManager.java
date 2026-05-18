package dev.helix.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.helix.hud.HudElement;
import dev.helix.hud.HudManager;
import dev.helix.module.Module;
import dev.helix.module.ModuleManager;
import dev.helix.setting.BooleanSetting;
import dev.helix.setting.NumberSetting;
import dev.helix.setting.Setting;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ConfigManager {
    private static final long AUTO_SAVE_INTERVAL_MS = 10_000L;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path root = FabricLoader.getInstance().getGameDir().resolve("Helix");
    private final Path configPath = root.resolve("configs").resolve("default.json");
    private HelixConfig loaded = new HelixConfig();
    private long lastSave;

    public void load() {
        createDirectories();
        if (!Files.exists(configPath)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(configPath)) {
            loaded = gson.fromJson(reader, HelixConfig.class);
            if (loaded == null) {
                loaded = new HelixConfig();
            }
        } catch (IOException exception) {
            System.err.println("[Helix] Failed to load config: " + exception.getMessage());
        }
    }

    public void apply(ModuleManager modules, HudManager hud) {
        if (loaded.modules != null) {
            for (Module module : modules.all()) {
                ModuleState state = loaded.modules.get(module.name());
                if (state == null) {
                    continue;
                }

                if (state.settings != null) {
                    applySettings(module, state.settings);
                }
                module.setEnabled(state.enabled);
            }
        }

        if (loaded.hud != null) {
            for (HudElement element : hud.elements()) {
                HudState state = loaded.hud.get(element.id());
                if (state != null) {
                    element.setPosition(state.x, state.y);
                    element.setScale(state.scale);
                    element.setVisible(state.visible);
                    element.setRainbow(state.rainbow);
                    element.setBackground(state.background);
                    element.setAccentColor(state.accentColor);
                }
            }
        }
    }

    public void autoSave(ModuleManager modules, HudManager hud) {
        long now = System.currentTimeMillis();
        if (now - lastSave >= AUTO_SAVE_INTERVAL_MS) {
            save(modules, hud);
            lastSave = now;
        }
    }

    public void save(ModuleManager modules, HudManager hud) {
        createDirectories();
        HelixConfig config = capture(modules, hud);
        try (Writer writer = Files.newBufferedWriter(configPath)) {
            gson.toJson(config, writer);
        } catch (IOException exception) {
            System.err.println("[Helix] Failed to save config: " + exception.getMessage());
        }
    }

    private HelixConfig capture(ModuleManager modules, HudManager hud) {
        HelixConfig config = new HelixConfig();
        for (Module module : modules.all()) {
            ModuleState state = new ModuleState();
            state.enabled = module.enabled();
            for (Setting<?> setting : module.settings()) {
                state.settings.put(setting.name(), setting.value());
            }
            config.modules.put(module.name(), state);
        }

        for (HudElement element : hud.elements()) {
            HudState state = new HudState();
            state.x = element.x();
            state.y = element.y();
            state.scale = element.scale();
            state.visible = element.visible();
            state.rainbow = element.rainbow();
            state.background = element.background();
            state.accentColor = element.accentColor();
            config.hud.put(element.id(), state);
        }

        return config;
    }

    private void applySettings(Module module, Map<String, Object> values) {
        for (Setting<?> setting : module.settings()) {
            Object value = values.get(setting.name());
            if (value instanceof Number number && setting instanceof NumberSetting numberSetting) {
                numberSetting.setValue(number.doubleValue());
            } else if (value instanceof Boolean bool && setting instanceof BooleanSetting booleanSetting) {
                booleanSetting.setValue(bool);
            }
        }
    }

    private void createDirectories() {
        try {
            Files.createDirectories(root.resolve("configs"));
            Files.createDirectories(root.resolve("assets"));
            Files.createDirectories(root.resolve("logs"));
            Files.createDirectories(root.resolve("themes"));
        } catch (IOException exception) {
            throw new IllegalStateException("Could not create Helix config directories", exception);
        }
    }

    private static final class HelixConfig {
        private Map<String, ModuleState> modules = new LinkedHashMap<>();
        private Map<String, HudState> hud = new LinkedHashMap<>();
    }

    private static final class ModuleState {
        private boolean enabled;
        private Map<String, Object> settings = new LinkedHashMap<>();
    }

    private static final class HudState {
        private int x;
        private int y;
        private double scale = 1.0;
        private boolean visible = true;
        private boolean rainbow;
        private boolean background = true;
        private int accentColor = 0xFF8A35FF;
    }
}
