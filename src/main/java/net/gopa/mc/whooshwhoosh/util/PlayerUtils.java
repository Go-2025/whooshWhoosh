package net.gopa.mc.whooshwhoosh.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public final class PlayerUtils {
    private PlayerUtils() {
    }

    /**
     * 获取攻击者攻击冷却进度是否达标，
     */
    public static boolean isAttackCooledDown(PlayerEntity player) {
        return player.getAttackCooldownProgress(0.5F) > 0.9F;
    }

    public static boolean isCritical(LivingEntity attacker, LivingEntity target) {
        if (attacker instanceof PlayerEntity player) {
            boolean f = isAttackCooledDown(player)
                    && player.fallDistance > 0.0F
                    && !player.isOnGround()
                    && !player.isClimbing()
                    && !player.isTouchingWater()
                    && !player.hasStatusEffect(StatusEffects.BLINDNESS)
                    && !player.hasVehicle()
                    && target instanceof LivingEntity;
            return f && !player.isSprinting();
        }
        return false;
    }
}
