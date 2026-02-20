package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.event.api.EntityAttackEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class EntityAttackMixin {

    @Inject(method = "onAttacking", at = @At("HEAD"))
    private void onAttack(Entity target, CallbackInfo ci) {
        if (target instanceof LivingEntity livingTarget) {
            LivingEntity self = (LivingEntity) (Object) this;
            EntityAttackEvent.EVENT.invoker().interact(livingTarget, self);
        }
    }
}
