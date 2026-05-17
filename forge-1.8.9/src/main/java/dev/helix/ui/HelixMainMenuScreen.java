package dev.helix.ui;

import dev.helix.render.RenderUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class HelixMainMenuScreen extends GuiScreen implements GuiYesNoCallback {
    private final List<Particle> particles = new ArrayList<Particle>();

    @Override
    public void initGui() {
        buttonList.clear();
        int center = width / 2;
        int top = height / 2 - 18;
        buttonList.add(new HelixButton(1, center - 78, top, 156, 22, "Singleplayer"));
        buttonList.add(new HelixButton(2, center - 78, top + 28, 156, 22, "Multiplayer"));
        buttonList.add(new HelixButton(3, center - 78, top + 56, 156, 22, "Options"));
        buttonList.add(new HelixButton(4, center - 78, top + 84, 156, 22, "Quit"));

        particles.clear();
        Random random = new Random(42L);
        for (int i = 0; i < 74; i++) {
            particles.add(new Particle(random.nextFloat(), random.nextFloat(), 0.35F + random.nextFloat() * 0.8F));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawGradientRect(0, 0, width, height, 0xFF05070B, 0xFF101820);
        drawHelix(partialTicks);
        drawCenteredString(fontRendererObj, "HELIX CLIENT", width / 2, height / 2 - 86, RenderUtil.WHITE);
        drawCenteredString(fontRendererObj, "1.8.9 Forge", width / 2, height / 2 - 70, RenderUtil.MUTED);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        } else if (button.id == 2) {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        } else if (button.id == 3) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        } else if (button.id == 4) {
            mc.displayGuiScreen(new GuiYesNo(this, "Quit Helix Client?", "", "Quit", "Cancel", 0));
        }
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (result) {
            mc.shutdown();
        } else {
            mc.displayGuiScreen(this);
        }
    }

    private void drawHelix(float partialTicks) {
        float time = (System.currentTimeMillis() % 120000L) / 900.0F + partialTicks;
        int cx = width / 2;
        int cy = height / 2 - 30;
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            float t = i / (float) particles.size();
            int y = (int) (cy - 105 + t * 210);
            double wave = Math.sin(t * Math.PI * 7.0D + time);
            int x1 = (int) (cx + wave * 96.0D);
            int x2 = (int) (cx - wave * 96.0D);
            int alpha = 70 + (int) (Math.abs(wave) * 80.0D);
            int color = (alpha << 24) | 0x35D8FF;
            drawRect(x1 - 1, y - 1, x1 + 2, y + 2, color);
            drawRect(x2 - 1, y - 1, x2 + 2, y + 2, color);
            if (i % 3 == 0) {
                drawHorizontalLine(Math.min(x1, x2), Math.max(x1, x2), y, 0x2235D8FF);
            }
            particle.drift += particle.speed * 0.002F;
        }
    }

    private static final class Particle {
        private final float x;
        private final float y;
        private final float speed;
        private float drift;

        private Particle(float x, float y, float speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }
    }
}
