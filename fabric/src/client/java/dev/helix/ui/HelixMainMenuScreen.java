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
        int top = height / 2 - 18;
        buttons.add(new MenuButton(1, center - 78, top, "Singleplayer"));
        buttons.add(new MenuButton(2, center - 78, top + 28, "Multiplayer"));
        buttons.add(new MenuButton(3, center - 78, top + 56, "Options"));
        buttons.add(new MenuButton(4, center - 78, top + 84, "Quit"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, width, height, 0xFF05070B, 0xFF101820);
        drawHelix(context, delta);
        context.drawCenteredTextWithShadow(textRenderer, "HELIX CLIENT", width / 2, height / 2 - 86, RenderUtil.WHITE);
        context.drawCenteredTextWithShadow(textRenderer, "Fabric", width / 2, height / 2 - 70, RenderUtil.MUTED);
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
        int cx = width / 2;
        int cy = height / 2 - 30;
        for (int i = 0; i < 74; i++) {
            float t = i / 74.0F;
            int y = (int) (cy - 105 + t * 210);
            double wave = Math.sin(t * Math.PI * 7.0D + time);
            int x1 = (int) (cx + wave * 96.0D);
            int x2 = (int) (cx - wave * 96.0D);
            int alpha = 70 + (int) (Math.abs(wave) * 80.0D);
            int color = (alpha << 24) | 0x35D8FF;
            context.fill(x1 - 1, y - 1, x1 + 2, y + 2, color);
            context.fill(x2 - 1, y - 1, x2 + 2, y + 2, color);
            if (i % 3 == 0) {
                context.fill(Math.min(x1, x2), y, Math.max(x1, x2), y + 1, 0x2235D8FF);
            }
        }
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
            RenderUtil.glassPanel(context, x, y, 156, 22, hovered);
            context.drawCenteredTextWithShadow(textRenderer, label, x + 78, y + 7, hovered ? RenderUtil.CYAN : RenderUtil.WHITE);
            if (hovered) {
                context.fill(x + 14, y + 18, x + 142, y + 19, 0xAA35D8FF);
            }
        }

        private boolean contains(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + 156 && mouseY >= y && mouseY <= y + 22;
        }
    }
}
