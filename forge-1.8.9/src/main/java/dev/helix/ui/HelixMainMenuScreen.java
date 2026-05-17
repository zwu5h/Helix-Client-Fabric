package dev.helix.ui;

import dev.helix.render.RenderUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;

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
        int top = height / 2 - 12;
        buttonList.add(new HelixButton(1, center - 128, top, 256, 44, "EINZELSPIELER"));
        buttonList.add(new HelixButton(2, center - 128, top + 54, 256, 44, "MEHRSPIELER"));
        buttonList.add(new HelixButton(3, center - 128, top + 108, 256, 44, "OPTIONEN"));
        buttonList.add(new HelixButton(4, center - 128, top + 162, 256, 44, "BEENDEN"));

        particles.clear();
        Random random = new Random(42L);
        for (int i = 0; i < 74; i++) {
            particles.add(new Particle(random.nextFloat(), random.nextFloat(), 0.35F + random.nextFloat() * 0.8F));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawGradientRect(0, 0, width, height, 0xFF03010A, 0xFF12072A);
        drawAmbientParticles(partialTicks);
        drawHelix(partialTicks);
        drawHelixLogo(width / 2, height / 2 - 150);
        drawCenteredScaledString("H  E  L  I  X", width / 2, height / 2 - 96, 0xFFF3F1FF, 2.4F);
        drawCenteredScaledString("C  L  I  E  N  T", width / 2, height / 2 - 58, 0xFF7A34E8, 1.4F);
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
            mc.displayGuiScreen(new GuiYesNo(this, "Helix Client beenden?", "", "Beenden", "Abbrechen", 0));
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
        int cx = (int) (width * 0.77F);
        int cy = height / 2;
        for (int i = 0; i < 118; i++) {
            float t = i / 118.0F;
            int y = (int) (cy - height * 0.52F + t * height * 1.05F);
            double wave = Math.sin(t * Math.PI * 9.0D + time);
            int radius = (int) (46 + 82 * (0.3F + t));
            int x1 = (int) (cx + wave * radius);
            int x2 = (int) (cx - wave * radius * 0.52D);
            int alpha = 55 + (int) (Math.abs(wave) * 110.0D);
            int color = (alpha << 24) | 0x7A34E8;
            drawRect(x1 - 1, y - 1, x1 + 2, y + 2, color);
            drawRect(x2 - 1, y - 1, x2 + 2, y + 2, color);
            if (i % 2 == 0) {
                drawHorizontalLine(Math.min(x1, x2), Math.max(x1, x2), y, 0x227A34E8);
            }
        }
    }

    private void drawAmbientParticles(float partialTicks) {
        float time = (System.currentTimeMillis() % 120000L) / 1000.0F + partialTicks;
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            int x = (int) ((particle.x * width + Math.sin(time * particle.speed + i) * 10.0D) % width);
            int y = (int) ((particle.y * height + Math.cos(time * particle.speed + i) * 6.0D) % height);
            int size = i % 9 == 0 ? 3 : 1;
            int alpha = i % 9 == 0 ? 0x66 : 0x99;
            drawRect(x, y, x + size, y + size, (alpha << 24) | 0x6C27D9);
        }
    }

    private void drawHelixLogo(int cx, int y) {
        drawRect(cx - 20, y, cx - 6, y + 8, 0xFF8A35FF);
        drawRect(cx - 20, y + 8, cx - 12, y + 34, 0xFF742FE0);
        drawRect(cx - 12, y + 28, cx + 3, y + 38, 0xFF9E58FF);
        drawRect(cx + 8, y, cx + 20, y + 38, 0xFF5624B8);
        drawRect(cx - 3, y + 18, cx + 8, y + 28, 0xFF7A34E8);
    }

    private void drawCenteredScaledString(String text, int x, int y, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        fontRendererObj.drawString(text, (int) (x / scale - fontRendererObj.getStringWidth(text) / 2.0F), (int) (y / scale), color);
        GlStateManager.popMatrix();
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
