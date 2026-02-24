package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.Handler.Handler;
import net.gopa.mc.whooshwhoosh.enchantment.BashEnchantment;
import net.gopa.mc.whooshwhoosh.event.api.EntityLandEvent;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.gopa.mc.whooshwhoosh.toolkit.util.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class, PlayerEntity.class})
public class EntityLandEnchMixin {

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void onTickEnd(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if ((Entity) (Object) this instanceof LivingEntity self && EntityUtils.isServer(self)) {
            ActionResult result = new Handler()
                    .addResult(((BashEnchantment) EnchantmentsRegistry.BASH.get()).onLand(self, fallDistance, damageSource))
                    .addResult(EntityLandEvent.EVENT.invoker().interact(self))
                    .result();
            if (result != ActionResult.PASS) cir.cancel();
        }
    }
}
