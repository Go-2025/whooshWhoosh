package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.Handler.EnchTriggerHandler;
import net.gopa.mc.whooshwhoosh.enchantment.SymbiosisEnchantment;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;


@Mixin(ItemStack.class)
public abstract class SymbiosisEnchMixin {

    @Inject(
            method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z"),
            cancellable = true,
            order = Integer.MAX_VALUE
    )
    private void onDamage(
            int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback, CallbackInfo ci
    ) {
        ActionResult result = new EnchTriggerHandler(TriggerPoint.OTHER)
                .handleEntity(entity, (SymbiosisEnchantment) EnchantmentsRegistry.SYMBIOSIS.get(), (ench, level) -> ench.onDamage(amount, entity, breakCallback))
                .result();
        if (result.isAccepted()) {
            ci.cancel();
        }
    }
}