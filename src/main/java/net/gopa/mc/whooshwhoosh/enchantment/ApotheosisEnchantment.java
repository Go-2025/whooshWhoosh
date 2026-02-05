package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Consumable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class ApotheosisEnchantment extends Enchantment implements Consumable {

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
    public void consume(Map<Enchantment, Integer> values, Enchantment self) {
        for (Map.Entry<Enchantment, Integer> entry : values.entrySet()) {
            Enchantment enchantment = entry.getKey();
            entry.setValue(Math.max(entry.getValue(), enchantment.getMaxLevel()));
        }
    }
}
