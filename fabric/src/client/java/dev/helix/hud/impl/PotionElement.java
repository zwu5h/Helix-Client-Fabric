package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class PotionElement extends HudElement {
    public PotionElement() {
        super("potions", "Potions", 8, 138);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        RenderUtil.panelText(context, client, x(), y(), "POT " + client.player.getStatusEffects().size());
    }

    @Override
    public int width(MinecraftClient client) {
        return RenderUtil.panelWidth(client, "POT 00");
    }
}
