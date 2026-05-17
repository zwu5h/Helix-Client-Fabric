package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;

public final class PotionElement extends HudElement {
    public PotionElement() {
        super("potions", "Potions", 8, 138);
    }

    @Override
    public void render(Minecraft minecraft) {
        RenderUtil.panelText(minecraft, getX(), getY(), "POT " + minecraft.thePlayer.getActivePotionEffects().size());
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        return RenderUtil.panelWidth(minecraft, "POT 00");
    }
}
