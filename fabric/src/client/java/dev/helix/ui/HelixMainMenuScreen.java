package dev.helix.ui;

import dev.helix.module.impl.PerformanceModule;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class HelixMainMenuScreen extends Screen {
    private final List<MenuButton> buttons = new ArrayList<>();
    private final List<Particle> particles = new ArrayList<>();
    private int particleWidth;
    private int particleHeight;

    public HelixMainMenuScreen() {
        super(Text.literal("Helix Client"));
    }

    @Override
    protected void init() {
        buttons.clear();
        int center = width / 2;
        int top = height / 2 - 12;
        buttons.add(new MenuButton(1, center - 122, top, "EINZELSPIELER"));
        buttons.add(new MenuButton(2, center - 122, top + 48, "MEHRSPIELER"));
        buttons.add(new MenuButton(3, center - 122, top + 96, "OPTIONEN"));
        buttons.add(new MenuButton(4, center - 122, top + 144, "BEENDEN"));
        initParticles();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, width, height, 0xFF04020A, 0xFF12091E);
        context.fillGradient(0, 0, width, height, 0x66110A22, 0x22000000);
        drawAmbientParticles(context, mouseX, mouseY, delta);
        drawHelix(context, mouseX, mouseY, delta);
        drawHelixLogo(context, width / 2, height / 2 - 150);
        drawCenteredScaledText(context, "H  E  L  I  X", width / 2, height / 2 - 96, 0xFFF3F1FF, 2.4F);
        drawCenteredScaledText(context, "C  L  I  E  N  T", width / 2, height / 2 - 58, 0xFF35D8FF, 1.4F);
        for (MenuButton button : buttons) {
            button.render(context, mouseX, mouseY);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        MinecraftClient client = MinecraftClient.getInstance();
        for (MenuButton button : buttons) {
            if (button.contains((int) click.x(), (int) click.y())) {
                if (button.id == 1) {
                    client.setScreen(new SelectWorldScreen(this));
                } else if (button.id == 2) {
                    client.setScreen(new MultiplayerScreen(this));
                } else if (button.id == 3) {
                    client.setScreen(new OptionsScreen(this, client.options));
                } else if (button.id == 4) {
                    client.scheduleStop();
                }
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private void drawHelix(DrawContext context, int mouseX, int mouseY, float delta) {
        long start = System.nanoTime();
        float time = (System.currentTimeMillis() % 180000L) / 2800.0F + delta * 0.02F;
        int points = PerformanceModule.lowPerformance() ? 54 : Math.max(64, Math.min(104, height / 7));
        int cx = (int) (width * 0.77F + (mouseX - width / 2.0F) * 0.018F);
        int cy = height / 2;
        int top = (int) (cy - height * 0.42F + (mouseY - height / 2.0F) * 0.012F);
        int span = (int) (height * 0.84F);
        for (int i = 0; i < points; i++) {
            float t = i / (float) points;
            double angle = t * Math.PI * 7.0D + time;
            int y = (int) (cy - height * 0.46F + t * height * 0.92F);
            y = top + (int) (t * span);
            double wave = Math.sin(angle);
            double depth = (Math.cos(angle) + 1.0D) * 0.5D;
            int radius = (int) (46 + 36 * Math.sin(t * Math.PI));
            int x1 = (int) (cx + wave * radius);
            int x2 = (int) (cx - wave * radius);
            int alpha1 = 42 + (int) (depth * 92.0D);
            int alpha2 = 42 + (int) ((1.0D - depth) * 92.0D);
            int size1 = depth > 0.55D ? 3 : 2;
            int size2 = depth < 0.45D ? 3 : 2;
            context.fill(x1 - size1 / 2, y - size1 / 2, x1 + size1, y + size1, (alpha1 << 24) | 0x8A35FF);
            context.fill(x2 - size2 / 2, y - size2 / 2, x2 + size2, y + size2, (alpha2 << 24) | 0x35D8FF);
            if (!PerformanceModule.lowPerformance() && i % 2 == 0) {
                context.fill(Math.min(x1, x2), y, Math.max(x1, x2), y + 1, 0x1FBA8CFF);
            }
        }
        logRenderTime("DNA", start);
    }

    private void drawAmbientParticles(DrawContext context, int mouseX, int mouseY, float delta) {
        long start = System.nanoTime();
        if (particles.isEmpty() || particleWidth != width || particleHeight != height || particles.size() != PerformanceModule.menuParticles()) {
            initParticles();
        }
        float parallaxX = (mouseX - width / 2.0F) * 0.012F;
        float parallaxY = (mouseY - height / 2.0F) * 0.010F;
        for (Particle particle : particles) {
            particle.x += particle.vx * delta;
            particle.y += particle.vy * delta;
            if (particle.x < 0) {
                particle.x += width;
            } else if (particle.x > width) {
                particle.x -= width;
            }
            if (particle.y < 0) {
                particle.y += height;
            } else if (particle.y > height) {
                particle.y -= height;
            }
            int x = (int) (particle.x + parallaxX * particle.depth);
            int y = (int) (particle.y + parallaxY * particle.depth);
            context.fill(x, y, x + particle.size, y + particle.size, (particle.alpha << 24) | 0xB98CFF);
        }
        logRenderTime("Particles", start);
    }

    private void initParticles() {
        particles.clear();
        particleWidth = width;
        particleHeight = height;
        Random random = new Random(0x48454C49584CL);
        int count = PerformanceModule.menuParticles();
        for (int i = 0; i < count; i++) {
            particles.add(new Particle(
                    random.nextFloat() * Math.max(1, width),
                    random.nextFloat() * Math.max(1, height),
                    (random.nextFloat() - 0.5F) * 0.22F,
                    (random.nextFloat() - 0.5F) * 0.18F,
                    0.4F + random.nextFloat() * 1.8F,
                    random.nextInt(3) == 0 ? 2 : 1,
                    22 + random.nextInt(42)
            ));
        }
    }

    private void logRenderTime(String label, long start) {
        if (PerformanceModule.profiling() && System.currentTimeMillis() % 1200L < 20L) {
            System.out.println("[Helix] MainMenu " + label + " " + ((System.nanoTime() - start) / 1_000_000.0D) + "ms");
        }
    }

    private void drawHelixLogo(DrawContext context, int cx, int y) {
        context.fill(cx - 20, y, cx - 6, y + 8, 0xFF8A35FF);
        context.fill(cx - 20, y + 8, cx - 12, y + 34, 0xFF742FE0);
        context.fill(cx - 12, y + 28, cx + 3, y + 38, 0xFF9E58FF);
        context.fill(cx + 8, y, cx + 20, y + 38, 0xFF5624B8);
        context.fill(cx - 3, y + 18, cx + 8, y + 28, 0xFF7A34E8);
    }

    private void drawCenteredScaledText(DrawContext context, String text, int x, int y, int color, float scale) {
        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale, scale);
        context.drawText(textRenderer, text, (int) (x / scale - textRenderer.getWidth(text) / 2.0F), (int) (y / scale), color, false);
        context.getMatrices().popMatrix();
    }

    private final class MenuButton {
        private final int id;
        private final int x;
        private final int y;
        private final String label;

        private MenuButton(int id, int x, int y, String label) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.label = label;
        }

        private void render(DrawContext context, int mouseX, int mouseY) {
            boolean hovered = contains(mouseX, mouseY);
            RenderUtil.glassPanel(context, x, y, 244, 40, hovered);
            context.fill(x, y, x + 244, y + 1, hovered ? 0xAA8A35FF : 0x668A35FF);
            context.fill(x, y + 39, x + 244, y + 40, hovered ? 0xAA35D8FF : 0x5535D8FF);
            int textX = x + 122 - textRenderer.getWidth(label) / 2;
            context.drawText(textRenderer, label, textX, y + 16, hovered ? RenderUtil.CYAN : RenderUtil.WHITE, false);
            if (hovered && !PerformanceModule.lowPerformance()) {
                context.fill(x + 10, y + 4, x + 234, y + 5, 0x4435D8FF);
                context.fill(x + 10, y + 35, x + 234, y + 36, 0x448A35FF);
            }
        }

        private boolean contains(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + 244 && mouseY >= y && mouseY <= y + 40;
        }
    }

    private static final class Particle {
        private float x;
        private float y;
        private final float vx;
        private final float vy;
        private final float depth;
        private final int size;
        private final int alpha;

        private Particle(float x, float y, float vx, float vy, float depth, int size, int alpha) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.depth = depth;
            this.size = size;
            this.alpha = alpha;
        }
    }
}
