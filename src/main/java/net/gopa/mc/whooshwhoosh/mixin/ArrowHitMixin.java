package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.event.api.ArrowHitEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class ArrowHitMixin {

    @Inject(method = "onEntityHit", at = @At(value = "HEAD"), cancellable = true)
    private void onHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        if ((Object) this instanceof ArrowEntity arrow) {
            Entity target = entityHitResult.getEntity();
            if (target instanceof LivingEntity livingTarget) {
                ActionResult result = ArrowHitEvent.EVENT.invoker().interact(arrow, livingTarget);
                if (!result.equals(ActionResult.PASS)) ci.cancel();
            }
        }
    }
}
