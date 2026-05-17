package dev.helix.hud;

import dev.helix.hud.impl.CoordinatesElement;
import dev.helix.hud.impl.CpsElement;
import dev.helix.hud.impl.FpsElement;
import dev.helix.hud.impl.KeystrokesElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HudManager {
    private final List<HudElement> elements = new ArrayList<>();

    public void bootstrap() {
        if (!elements.isEmpty()) {
            return;
        }

        elements.add(new FpsElement());
        elements.add(new CpsElement());
        elements.add(new CoordinatesElement());
        elements.add(new KeystrokesElement());
    }

    public void tick(MinecraftClient client) {
        for (HudElement element : elements) {
            element.tick(client);
        }
    }

    public void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden || client.player == null) {
            return;
        }

        for (HudElement element : elements) {
            element.render(context, client);
        }
    }

    public List<HudElement> elements() {
        return Collections.unmodifiableList(elements);
    }
}
