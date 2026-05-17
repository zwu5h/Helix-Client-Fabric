package dev.helix.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.helix.hud.HudElement;
import dev.helix.hud.HudManager;
import dev.helix.module.Module;
import dev.helix.module.ModuleManager;
import dev.helix.setting.NumberSetting;
import dev.helix.setting.Setting;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ConfigManager {
    private static final long AUTO_SAVE_INTERVAL_MS = 10000L;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File root = new File(Minecraft.getMinecraft().mcDataDir, "Helix");
    private final File configFile = new File(new File(root, "configs"), "default-1.8.9.json");
    private HelixConfig loaded = new HelixConfig();
    private long lastSave;

    public void load() {
        createDirectories();
        if (!configFile.exists()) {
            return;
        }

        FileReader reader = null;
        try {
            reader = new FileReader(configFile);
            loaded = gson.fromJson(reader, HelixConfig.class);
            if (loaded == null) {
                loaded = new HelixConfig();
            }
        } catch (IOException exception) {
            System.err.println("[Helix] Failed to load config: " + exception.getMessage());
        } finally {
            close(reader);
        }
    }

    public void apply(ModuleManager modules, HudManager hud) {
        if (loaded.modules != null) {
            for (Module module : modules.all()) {
                ModuleState state = loaded.modules.get(module.getName());
                if (state == null) {
                    continue;
                }

                module.setEnabled(state.enabled);
                if (state.settings != null) {
                    applySettings(module, state.settings);
                }
            }
        }

        if (loaded.hud != null) {
            for (HudElement element : hud.elements()) {
                HudState state = loaded.hud.get(element.getId());
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
        FileWriter writer = null;
        try {
            writer = new FileWriter(configFile);
            gson.toJson(config, writer);
        } catch (IOException exception) {
            System.err.println("[Helix] Failed to save config: " + exception.getMessage());
        } finally {
            close(writer);
        }
    }

    private HelixConfig capture(ModuleManager modules, HudManager hud) {
        HelixConfig config = new HelixConfig();
        for (Module module : modules.all()) {
            ModuleState state = new ModuleState();
            state.enabled = module.isEnabled();
            for (Setting setting : module.getSettings()) {
                state.settings.put(setting.getName(), setting.getRawValue());
            }
            config.modules.put(module.getName(), state);
        }

        for (HudElement element : hud.elements()) {
            HudState state = new HudState();
            state.x = element.getX();
            state.y = element.getY();
            state.scale = element.getScale();
            state.visible = element.isVisible();
            state.rainbow = element.isRainbow();
            state.background = element.hasBackground();
            state.accentColor = element.getAccentColor();
            config.hud.put(element.getId(), state);
        }
        return config;
    }

    private void applySettings(Module module, Map<String, Object> values) {
        for (Setting setting : module.getSettings()) {
            Object value = values.get(setting.getName());
            if (value instanceof Number && setting instanceof NumberSetting) {
                ((NumberSetting) setting).setValue(((Number) value).doubleValue());
            }
        }
    }

    private void createDirectories() {
        new File(root, "configs").mkdirs();
        new File(root, "assets").mkdirs();
        new File(root, "logs").mkdirs();
        new File(root, "themes").mkdirs();
    }

    private void close(java.io.Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

    private static final class HelixConfig {
        private Map<String, ModuleState> modules = new LinkedHashMap<String, ModuleState>();
        private Map<String, HudState> hud = new LinkedHashMap<String, HudState>();
    }

    private static final class ModuleState {
        private boolean enabled;
        private Map<String, Object> settings = new LinkedHashMap<String, Object>();
    }

    private static final class HudState {
        private int x;
        private int y;
        private double scale = 1.0D;
        private boolean visible = true;
        private boolean rainbow;
        private boolean background = true;
        private int accentColor = 0xFF8A35FF;
    }
}
