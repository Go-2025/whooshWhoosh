package net.gopa.mc.whooshwhoosh.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

public final class StatusEffectUtils {

    private StatusEffectUtils() {
    }

    public static void addStatusEffects(LivingEntity target, StatusEffectInstance... effects) {
        for (StatusEffectInstance effect : effects) {
            target.addStatusEffect(effect);
        }
    }
    public static void addStatusEffects(LivingEntity target, LivingEntity source, StatusEffectInstance... effects) {
        for (StatusEffectInstance effect : effects) {
            target.addStatusEffect(effect, source);
        }
    }
}
