package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.Handler.EnchTriggerHandler;
import net.gopa.mc.whooshwhoosh.enchantment.VaultEnchantment;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerPlayerEntity.class})
public abstract class VaultEnchMixin {

    @Unique
    private boolean lastSneakState = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        boolean currentSneak = self.isSneaking();

        if (!currentSneak && lastSneakState) {
            ActionResult result = new EnchTriggerHandler(TriggerPoint.OTHER)
                    .handleEntity(self, (VaultEnchantment) EnchantmentsRegistry.VAULT.get(), (t, l) -> t.onOffSneak(l, self))
                    .result();
        }
        lastSneakState = currentSneak;
    }
}
