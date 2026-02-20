package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.function.Consumer;

public interface Triggerable {

    default ActionResult onAttack(int level, LivingEntity source, LivingEntity target) { throw new UnsupportedOperationException(); }
    default ActionResult onTargetDamage(int level, LivingEntity source, Entity attacker, DamageSource damageSource) { throw new UnsupportedOperationException(); }
    default ActionResult onCriticalHit(int level, Entity source, LivingEntity target) { throw new UnsupportedOperationException(); }
    default ActionResult onItemDamage(int level, LivingEntity source, ItemStack stack, int amount, Consumer<LivingEntity> breakCallback) { throw new UnsupportedOperationException(); }
    default ActionResult onItemBreak(int level, LivingEntity source, ItemStack stack, Consumer<LivingEntity> breakCallback) { throw new UnsupportedOperationException(); }
    default ActionResult onArrowHit(int level, LivingEntity target, ArrowEntity arrow) { throw new UnsupportedOperationException(); }
    default ActionResult onEntityJump(int level, LivingEntity source) { throw new UnsupportedOperationException(); }

    default boolean canTrigger(int level) {
        return won(level);
    }
    default boolean canTrigger(Entity entity, int level) {
        return canTrigger(level)
                && this instanceof Cooldown cooldown
                && entity != null
                && cooldown.isCooldownExpired(entity, level)
                ;
    }

    default boolean won(int level) {
        return level > 0
                && (getProbability(level) == 1d || (getProbability(level) != 0d)
                && WhooshwhooshMod.RANDOM.nextDouble() < getProbability(level));
    }

    default double getProbability(int level) {
        return 1d;  // (0, 1]
    }
}
