package net.gopa.mc.whooshwhoosh.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public abstract class ConsumableEnchantment extends Enchantment {

    public ConsumableEnchantment(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(rarity, target, slotTypes);
    }

    protected ConsumableEnchantment(Rarity rarity, EnchantmentTarget target, EquipmentSlot slotType) {
        super(rarity, target, new EquipmentSlot[]{slotType});
    }
}
