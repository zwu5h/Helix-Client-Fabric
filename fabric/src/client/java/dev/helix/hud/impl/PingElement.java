package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;

public final class PingElement extends HudElement {
    public PingElement() {
        super("ping", "Ping", 8, 158);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        PlayerListEntry entry = client.getNetworkHandler() == null ? null : client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        renderPanel(context, client, "PING " + (entry == null ? "--" : entry.getLatency()));
    }

    @Override
    public int width(MinecraftClient client) {
        return panelWidth(client, "PING 000");
    }
}
