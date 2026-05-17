package dev.helix.hud;

import dev.helix.hud.impl.CoordinatesElement;
import dev.helix.hud.impl.CpsElement;
import dev.helix.hud.impl.FpsElement;
import dev.helix.hud.impl.KeystrokesElement;
import dev.helix.hud.impl.ArmorElement;
import dev.helix.hud.impl.ActiveModulesElement;
import dev.helix.hud.impl.ClockElement;
import dev.helix.hud.impl.DirectionElement;
import dev.helix.hud.impl.MemoryElement;
import dev.helix.hud.impl.PingElement;
import dev.helix.hud.impl.PotionElement;
import dev.helix.hud.impl.SessionElement;
import dev.helix.hud.impl.SpeedElement;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HudManager {
    private final List<HudElement> elements = new ArrayList<HudElement>();

    public void bootstrap() {
        if (!elements.isEmpty()) {
            return;
        }

        elements.add(new FpsElement());
        elements.add(new CpsElement());
        elements.add(new CoordinatesElement());
        elements.add(new KeystrokesElement());
        elements.add(new ArmorElement());
        elements.add(new PotionElement());
        elements.add(new PingElement());
        elements.add(new DirectionElement());
        elements.add(new SpeedElement());
        elements.add(new MemoryElement());
        elements.add(new ClockElement());
        elements.add(new SessionElement());
        elements.add(new ActiveModulesElement());
    }

    public void tick(Minecraft minecraft) {
        for (HudElement element : elements) {
            element.tick(minecraft);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.gameSettings.showDebugInfo || minecraft.gameSettings.hideGUI || minecraft.thePlayer == null) {
            return;
        }

        for (HudElement element : elements) {
            if (element.isVisible()) {
                element.render(minecraft);
            }
        }
    }

    public List<HudElement> elements() {
        return Collections.unmodifiableList(elements);
    }
}
