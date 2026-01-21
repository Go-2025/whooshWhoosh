package net.gopa.mc.whooshwhoosh.enchantment;

import net.minecraft.enchantment.VanishingCurseEnchantment;
import net.minecraft.entity.EquipmentSlot;

public class OfferingEnchantment extends VanishingCurseEnchantment {
    public OfferingEnchantment() {
        super(Rarity.VERY_RARE, EquipmentSlot.values());
    }
}
