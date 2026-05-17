package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public final class ArmorElement extends HudElement {
    public ArmorElement() {
        super("armor", "Armor", 8, 118);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        int durability = 0;
        int pieces = 0;
        EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        for (EquipmentSlot slot : slots) {
            ItemStack stack = client.player.getEquippedStack(slot);
            if (!stack.isEmpty() && stack.isDamageable()) {
                durability += 100 - (stack.getDamage() * 100 / stack.getMaxDamage());
                pieces++;
            }
        }
        RenderUtil.panelText(context, client, x(), y(), "ARM " + (pieces == 0 ? "--" : durability / pieces + "%"));
    }

    @Override
    public int width(MinecraftClient client) {
        return RenderUtil.panelWidth(client, "ARM 100%");
    }
}
