package dev.helix.ui;

import dev.helix.HelixClient;
import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class HudEditorScreen extends Screen {
    private HudElement dragging;
    private int dragOffsetX;
    private int dragOffsetY;

    public HudEditorScreen() {
        super(Text.literal("Helix HUD Editor"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        drawGrid(context);
        MinecraftClient client = MinecraftClient.getInstance();
        for (HudElement element : HelixClient.HUD.elements()) {
            boolean hovered = element.contains(client, mouseX, mouseY);
            context.fill(element.x() - 2, element.y() - 2, element.x() + element.width(client) + 2, element.y() + element.height(client) + 2, hovered ? 0x6635D8FF : 0x33101822);
            element.render(context, client);
        }
        context.drawText(textRenderer, "HUD EDITOR", 12, 12, RenderUtil.CYAN, false);
        context.drawText(textRenderer, "Drag elements | Mouse wheel scales | H/Esc saves", 12, 24, RenderUtil.WHITE, false);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        MinecraftClient client = MinecraftClient.getInstance();
        for (HudElement element : HelixClient.HUD.elements()) {
            if (element.contains(client, (int) click.x(), (int) click.y())) {
                dragging = element;
                dragOffsetX = (int) click.x() - element.x();
                dragOffsetY = (int) click.y() - element.y();
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        if (dragging != null) {
            MinecraftClient client = MinecraftClient.getInstance();
            int snappedX = Math.max(2, Math.min(width - dragging.width(client) - 2, (int) click.x() - dragOffsetX));
            int snappedY = Math.max(2, Math.min(height - dragging.height(client) - 2, (int) click.y() - dragOffsetY));
            dragging.setPosition((snappedX / 4) * 4, (snappedY / 4) * 4);
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        dragging = null;
        HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        MinecraftClient client = MinecraftClient.getInstance();
        for (HudElement element : HelixClient.HUD.elements()) {
            if (element.contains(client, (int) mouseX, (int) mouseY)) {
                element.setScale(element.scale() + (verticalAmount > 0 ? 0.1D : -0.1D));
                HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == GLFW.GLFW_KEY_H || input.key() == GLFW.GLFW_KEY_ESCAPE) {
            HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
            close();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void drawGrid(DrawContext context) {
        for (int x = 0; x < width; x += 16) {
            context.fill(x, 0, x + 1, height, 0x22101A24);
        }
        for (int y = 0; y < height; y += 16) {
            context.fill(0, y, width, y + 1, 0x22101A24);
        }
    }
}
