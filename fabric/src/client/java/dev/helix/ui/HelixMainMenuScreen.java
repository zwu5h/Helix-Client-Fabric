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
        int top = height / 2 - 12;
        buttons.add(new MenuButton(1, center - 128, top, "EINZELSPIELER"));
        buttons.add(new MenuButton(2, center - 128, top + 54, "MEHRSPIELER"));
        buttons.add(new MenuButton(3, center - 128, top + 108, "OPTIONEN"));
        buttons.add(new MenuButton(4, center - 128, top + 162, "BEENDEN"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, width, height, 0xFF03010A, 0xFF12072A);
        drawAmbientParticles(context, delta);
        drawHelix(context, delta);
        drawHelixLogo(context, width / 2, height / 2 - 150);
        drawCenteredScaledText(context, "H  E  L  I  X", width / 2, height / 2 - 96, 0xFFF3F1FF, 2.4F);
        drawCenteredScaledText(context, "C  L  I  E  N  T", width / 2, height / 2 - 58, 0xFF7A34E8, 1.4F);
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
        float time = (System.currentTimeMillis() % 120000L) / 900.0F + delta;
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
            context.fill(x1 - 1, y - 1, x1 + 2, y + 2, color);
            context.fill(x2 - 1, y - 1, x2 + 2, y + 2, color);
            if (i % 2 == 0) {
                context.fill(Math.min(x1, x2), y, Math.max(x1, x2), y + 1, 0x227A34E8);
            }
        }
    }

    private void drawAmbientParticles(DrawContext context, float delta) {
        float time = (System.currentTimeMillis() % 120000L) / 1000.0F + delta;
        for (int i = 0; i < 80; i++) {
            float seedX = ((i * 37) % 100) / 100.0F;
            float seedY = ((i * 61) % 100) / 100.0F;
            int x = (int) ((seedX * width + Math.sin(time * (0.35F + i * 0.01F) + i) * 10.0D) % width);
            int y = (int) ((seedY * height + Math.cos(time * (0.42F + i * 0.01F) + i) * 6.0D) % height);
            int size = i % 9 == 0 ? 3 : 1;
            int alpha = i % 9 == 0 ? 0x66 : 0x99;
            context.fill(x, y, x + size, y + size, (alpha << 24) | 0x6C27D9);
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
            RenderUtil.glassPanel(context, x, y, 256, 44, hovered);
            context.fill(x, y, x + 1, y + 44, hovered ? 0xCCB45CFF : 0x665A25B8);
            context.fill(x + 255, y, x + 256, y + 44, hovered ? 0xCCB45CFF : 0x665A25B8);
            drawIcon(context, hovered);
            context.drawText(textRenderer, label, x + 86, y + 18, hovered ? RenderUtil.CYAN : RenderUtil.WHITE, false);
            if (hovered) {
                context.fill(x + 1, y + 1, x + 255, y + 2, 0x99B45CFF);
            }
        }

        private boolean contains(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + 256 && mouseY >= y && mouseY <= y + 44;
        }

        private void drawIcon(DrawContext context, boolean hovered) {
            int cx = x + 42;
            int cy = y + 22;
            int purple = hovered ? 0xFFB45CFF : 0xFF7A34E8;
            if (id == 1) {
                context.fill(cx - 5, cy - 8, cx - 2, cy + 8, purple);
                context.fill(cx - 2, cy - 5, cx + 4, cy + 5, purple);
                context.fill(cx + 4, cy - 2, cx + 7, cy + 2, purple);
            } else if (id == 2) {
                context.fill(cx - 10, cy - 2, cx - 4, cy + 7, purple);
                context.fill(cx - 7, cy - 9, cx - 1, cy - 3, purple);
                context.fill(cx + 2, cy - 2, cx + 8, cy + 7, purple);
                context.fill(cx + 1, cy - 9, cx + 7, cy - 3, purple);
            } else if (id == 3) {
                context.fill(cx - 7, cy - 7, cx + 7, cy + 7, purple);
                context.fill(cx - 3, cy - 3, cx + 3, cy + 3, 0xAA0B0616);
            } else {
                context.fill(cx - 9, cy - 9, cx + 5, cy + 9, purple);
                context.fill(cx - 6, cy - 6, cx + 2, cy + 6, 0xAA0B0616);
                context.fill(cx + 2, cy - 2, cx + 10, cy + 2, purple);
            }
        }
    }
}
