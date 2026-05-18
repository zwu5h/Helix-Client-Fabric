package dev.helix.ui;

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

public final class HelixMainMenuScreen extends Screen {
    private final List<MenuButton> buttons = new ArrayList<>();

    public HelixMainMenuScreen() {
        super(Text.literal("Helix Client"));
    }

    @Override
    protected void init() {
        buttons.clear();
        int center = width / 2;
        int top = height / 2 - 8;
        buttons.add(new MenuButton(1, center - 132, top, "EINZELSPIELER"));
        buttons.add(new MenuButton(2, center - 132, top + 50, "MEHRSPIELER"));
        buttons.add(new MenuButton(3, center - 132, top + 100, "OPTIONEN"));
        buttons.add(new MenuButton(4, center - 132, top + 150, "BEENDEN"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, width, height, 0xFF03050A, 0xFF0D1018);
        drawAmbientParticles(context, delta);
        drawHelix(context, delta);
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

    private void drawHelix(DrawContext context, float delta) {
        float time = (System.currentTimeMillis() % 180000L) / 2800.0F + delta * 0.02F;
        int cx = (int) (width * 0.77F);
        int cy = height / 2;
        for (int i = 0; i < 96; i++) {
            float t = i / 96.0F;
            int y = (int) (cy - height * 0.46F + t * height * 0.92F);
            double wave = Math.sin(t * Math.PI * 7.0D + time);
            int radius = (int) (38 + 64 * (0.2F + t));
            int x1 = (int) (cx + wave * radius);
            int x2 = (int) (cx - wave * radius * 0.56D);
            int alpha = 34 + (int) (Math.abs(wave) * 72.0D);
            int color = (alpha << 24) | 0x35D8FF;
            context.fill(x1 - 1, y - 1, x1 + 2, y + 2, color);
            context.fill(x2 - 1, y - 1, x2 + 2, y + 2, color);
            if (i % 3 == 0) {
                context.fill(Math.min(x1, x2), y, Math.max(x1, x2), y + 1, 0x1835D8FF);
            }
        }
    }

    private void drawAmbientParticles(DrawContext context, float delta) {
        float time = (System.currentTimeMillis() % 180000L) / 2600.0F + delta * 0.02F;
        for (int i = 0; i < 38; i++) {
            float seedX = ((i * 37) % 100) / 100.0F;
            float seedY = ((i * 61) % 100) / 100.0F;
            int x = (int) ((seedX * width + Math.sin(time * (0.18F + i * 0.004F) + i) * 6.0D) % width);
            int y = (int) ((seedY * height + Math.cos(time * (0.22F + i * 0.004F) + i) * 4.0D) % height);
            int size = i % 11 == 0 ? 2 : 1;
            int alpha = i % 11 == 0 ? 0x44 : 0x33;
            context.fill(x, y, x + size, y + size, (alpha << 24) | 0x35D8FF);
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
            RenderUtil.glassPanel(context, x, y, 264, 42, hovered);
            context.fill(x, y, x + 2, y + 42, hovered ? 0xAA35D8FF : 0x5535D8FF);
            context.drawText(textRenderer, label, x + 28, y + 17, hovered ? RenderUtil.CYAN : RenderUtil.WHITE, false);
            if (hovered) {
                context.fill(x + 2, y + 1, x + 262, y + 2, 0x6635D8FF);
            }
        }

        private boolean contains(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + 264 && mouseY >= y && mouseY <= y + 42;
        }
    }
}
