package net.gopa.mc.whooshwhoosh.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class SymbiosisEnchantment extends Enchantment {
    public SymbiosisEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.BREAKABLE, EquipmentSlot.values());
    }
}
