package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

import java.util.ArrayDeque;
import java.util.Deque;

public final class CpsElement extends HudElement {
    private final Deque<Long> clicks = new ArrayDeque<Long>();
    private boolean previousDown;

    public CpsElement() {
        super("cps", 8, 28);
    }

    @Override
    public void tick(Minecraft minecraft) {
        boolean down = Mouse.isButtonDown(0);
        if (down && !previousDown) {
            clicks.addLast(System.currentTimeMillis());
        }
        previousDown = down;

        long cutoff = System.currentTimeMillis() - 1000L;
        while (!clicks.isEmpty() && clicks.peekFirst().longValue() < cutoff) {
            clicks.removeFirst();
        }
    }

    @Override
    public void render(Minecraft minecraft) {
        RenderUtil.panelText(minecraft, getX(), getY(), "CPS " + clicks.size());
    }
}
