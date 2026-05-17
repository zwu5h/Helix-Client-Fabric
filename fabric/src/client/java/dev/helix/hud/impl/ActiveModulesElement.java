package dev.helix.hud.impl;

import dev.helix.HelixClient;
import dev.helix.hud.HudElement;
import dev.helix.module.Module;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class ActiveModulesElement extends HudElement {
    public ActiveModulesElement() {
        super("active_modules", "Active Modules", 8, 278);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        int y = y();
        int rendered = 0;
        for (Module module : HelixClient.MODULES.all()) {
            if (module.enabled()) {
                RenderUtil.panelText(context, client, x(), y, module.name(), currentAccentColor(), background());
                y += 18;
                rendered++;
            }
        }
        if (rendered == 0) {
            renderPanel(context, client, "No modules");
        }
    }

    @Override
    public int width(MinecraftClient client) {
        int width = panelWidth(client, "No modules");
        for (Module module : HelixClient.MODULES.all()) {
            if (module.enabled()) {
                width = Math.max(width, panelWidth(client, module.name()));
            }
        }
        return width;
    }

    @Override
    public int height(MinecraftClient client) {
        int count = 0;
        for (Module module : HelixClient.MODULES.all()) {
            if (module.enabled()) {
                count++;
            }
        }
        return Math.max(1, count) * 18;
    }
}
