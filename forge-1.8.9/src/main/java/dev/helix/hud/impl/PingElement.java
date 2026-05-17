package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

public final class PingElement extends HudElement {
    public PingElement() {
        super("ping", "Ping", 8, 158);
    }

    @Override
    public void render(Minecraft minecraft) {
        NetworkPlayerInfo info = minecraft.getNetHandler() == null ? null : minecraft.getNetHandler().getPlayerInfo(minecraft.thePlayer.getUniqueID());
        RenderUtil.panelText(minecraft, getX(), getY(), "PING " + (info == null ? "--" : info.getResponseTime()));
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        return RenderUtil.panelWidth(minecraft, "PING 000");
    }
}
