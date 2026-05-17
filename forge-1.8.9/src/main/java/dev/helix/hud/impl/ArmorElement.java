package dev.helix.hud.impl;

import dev.helix.hud.HudElement;
import dev.helix.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public final class ArmorElement extends HudElement {
    public ArmorElement() {
        super("armor", "Armor", 8, 118);
    }

    @Override
    public void render(Minecraft minecraft) {
        int durability = 0;
        int pieces = 0;
        for (ItemStack stack : minecraft.thePlayer.inventory.armorInventory) {
            if (stack != null && stack.isItemStackDamageable()) {
                durability += 100 - (stack.getItemDamage() * 100 / stack.getMaxDamage());
                pieces++;
            }
        }
        renderPanel(minecraft, "ARM " + (pieces == 0 ? "--" : durability / pieces + "%"));
    }

    @Override
    public int getWidth(Minecraft minecraft) {
        return panelWidth(minecraft, "ARM 100%");
    }
}
