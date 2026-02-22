package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.event.api.EntityJumpEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class, PlayerEntity.class})
public abstract class EntityJumpMixin {

    @Inject(method = "jump", at = @At("HEAD"))
    private void jump(CallbackInfo ci) {
        EntityJumpEvent.EVENT.invoker().interact((LivingEntity) (Object) this);
    }
}
