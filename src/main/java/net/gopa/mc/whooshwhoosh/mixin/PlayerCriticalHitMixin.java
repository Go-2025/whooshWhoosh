package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.event.impl.PlayerCriticalHitListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.gopa.mc.whooshwhoosh.util.EntityUtils.isCritical;

@Mixin(PlayerEntity.class)
public class PlayerCriticalHitMixin {

    @Inject(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V")
    )
    private void onAttack(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (target instanceof LivingEntity livingTarget) {
            if (isCritical(player, livingTarget)) {
                PlayerCriticalHitListener.EVENT.invoker().interact(livingTarget, player);
            }
        }
    }
}
