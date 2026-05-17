package dev.helix.hud.impl;

import dev.helix.HelixClient;
import dev.helix.hud.HudElement;
import dev.helix.module.Module;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;

public final class ActiveModulesElement extends HudElement {
    public ActiveModulesElement() {
        super("active_modules", "Active Modules", 8, 278);
    }

    @Override
    public void render(Minecraft minecraft) {
        int y = getY();
        int rendered = 0;
        for (Module module : HelixClient.MODULES.all()) {
            if (module.isEnabled()) {
                RenderUtil.panelText(minecraft, getX(), y, module.getName(), currentAccentColor(), hasBackground());
                y += 18;
                rendered++;
            }
        }
        if (rendered == 0) {
            renderPanel(minecraft, "No modules");
        }
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        int width = panelWidth(minecraft, "No modules");
        for (Module module : HelixClient.MODULES.all()) {
            if (module.isEnabled()) {
                width = Math.max(width, panelWidth(minecraft, module.getName()));
            }
        }
        return width;
    }

    @Override
    public int getHeight(Minecraft minecraft) {
        int count = 0;
        for (Module module : HelixClient.MODULES.all()) {
            if (module.isEnabled()) {
                count++;
            }
        }
        return Math.max(1, count) * 18;
    }
}
