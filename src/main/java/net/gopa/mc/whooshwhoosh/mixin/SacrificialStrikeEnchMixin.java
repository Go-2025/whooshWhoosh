package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.Handler.EnchTriggerHandler;
import net.gopa.mc.whooshwhoosh.enchantment.SacrificialStrikeEnchantment;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class SacrificialStrikeEnchMixin {

    @Inject(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V")
    )
    private void onAttack(Entity target, CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        ActionResult res = new EnchTriggerHandler(TriggerPoint.OTHER)
                .handleEntity(self, (SacrificialStrikeEnchantment) EnchantmentsRegistry.SACRIFICIAL_STRIKE.get(),
                        (e, l) -> e.onPlayerAttack(l, self, target))
                .result();
    }
}
