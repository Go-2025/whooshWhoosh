package net.gopa.mc.whooshwhoosh.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ApotheosisEnchantment extends ModEnchantment {

    public ApotheosisEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.VANISHABLE, EquipmentSlot.values());
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof Item;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isConsumable() {
        return true;
    }
}
