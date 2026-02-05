package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends ProjectileEntity {

    @Unique
    private static final Enchantment CATALYSIS_ENCHANT = EnchantmentsRegistry.CATALYSIS.get();

    protected ArrowEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onHit", at = @At("HEAD"))
    private void onHit(LivingEntity target, CallbackInfo ci) {
        if (!target.isAlive()) return;
        int level = getCatalysisLevel();
        if (level < 0) return;
        processTargetEffects(target, level);
    }

    @Unique
    private int getCatalysisLevel() {
        if (this.getOwner() instanceof LivingEntity user) {
            return EnchantmentHelper.get(user.getMainHandStack()).getOrDefault(CATALYSIS_ENCHANT, -1);
        }
        return -1;
    }

    @Unique
    private void processTargetEffects(LivingEntity target, int level) {
        for (StatusEffectInstance effect : target.getStatusEffects()) {
            int oldDuration = effect.getDuration();
            int oldAmplifier = effect.getAmplifier();
            int newDuration = oldDuration / 3;
            int newAmplifier = oldAmplifier == 0 ? 1 : Math.min(oldAmplifier * 2, 225);
            StatusEffectInstance newEffect = new StatusEffectInstance(
                    effect.getEffectType(), newDuration, newAmplifier);
            target.setStatusEffect(newEffect, this);
        }
    }
}
