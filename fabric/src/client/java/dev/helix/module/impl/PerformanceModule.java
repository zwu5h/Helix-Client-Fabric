package dev.helix.module.impl;

import dev.helix.module.Category;
import dev.helix.module.Module;
import dev.helix.setting.BooleanSetting;
import dev.helix.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;

public final class PerformanceModule extends Module {
    private static boolean lowPerformance;
    private static boolean profiling;
    private static int menuParticles = 112;

    private final BooleanSetting lowPerformanceMode = addSetting(new BooleanSetting("Low Performance Mode", false));
    private final BooleanSetting renderProfiling = addSetting(new BooleanSetting("Render Profiling", false));
    private final NumberSetting particles = addSetting(new NumberSetting("Menu Particles", 112.0, 40.0, 160.0, 8.0));

    public PerformanceModule() {
        super("Performance", "Reduces expensive Helix menu and HUD effects when FPS matter.", Category.VISUAL);
        setEnabled(true);
    }

    @Override
    public void tick(MinecraftClient client) {
        lowPerformance = lowPerformanceMode.value();
        profiling = renderProfiling.value();
        menuParticles = particles.value().intValue();
    }

    public static boolean lowPerformance() {
        return lowPerformance;
    }

    public static boolean profiling() {
        return profiling;
    }

    public static int menuParticles() {
        return lowPerformance ? Math.min(64, menuParticles) : menuParticles;
    }
}
