package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.hasEnch;

@Deprecated(since = "1.0.0")
public interface DeadRattle {

    void deadrattle(int level, ItemStack stack, LivingEntity entity, Consumer<LivingEntity> breakCallback);

    default void onBreak(int level, ItemStack stack, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
        if (!canTrigger(stack)) return;
        deadrattle(level, stack, entity, breakCallback);
    }

    default boolean canTrigger(ItemStack stack) {
        return hasEnch(stack.getEnchantments(), (Enchantment) this);
    }
}