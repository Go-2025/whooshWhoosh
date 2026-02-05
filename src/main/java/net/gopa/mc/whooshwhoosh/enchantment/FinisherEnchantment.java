package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class FinisherEnchantment extends Enchantment implements Triggerable {

    public FinisherEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof SwordItem || item instanceof AxeItem;
    }

    @Override
    public int getMinPower(int level) {
        return (int) Math.pow(2.6, level) + 1;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 36;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

}
