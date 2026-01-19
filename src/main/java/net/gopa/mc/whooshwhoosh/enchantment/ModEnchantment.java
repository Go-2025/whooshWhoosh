package net.gopa.mc.whooshwhoosh.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public abstract class ModEnchantment extends Enchantment {

    public ModEnchantment(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(rarity, target, slotTypes);
    }

    protected ModEnchantment(Rarity rarity, EnchantmentTarget target, EquipmentSlot slotType) {
        super(rarity, target, new EquipmentSlot[]{slotType});
    }

    public boolean isConsumable() {
        return false;
    }

    public static boolean isConsumable(Enchantment ench) {
        return ench instanceof ModEnchantment && ((ModEnchantment) ench).isConsumable();
    }
}
