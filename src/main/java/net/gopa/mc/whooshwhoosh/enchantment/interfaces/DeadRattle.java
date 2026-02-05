package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.hasThisEnch;

public interface DeadRattle {

    void deadrattle(ItemStack stack, LivingEntity entity, Consumer<LivingEntity> breakCallback);

    default void onBreak(ItemStack stack, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
        if (!canTrigger(stack)) return;
        deadrattle(stack, entity, breakCallback);
    }

    default boolean canTrigger(ItemStack stack) {
        return hasThisEnch(stack.getEnchantments(), (Enchantment) this);
    }
}