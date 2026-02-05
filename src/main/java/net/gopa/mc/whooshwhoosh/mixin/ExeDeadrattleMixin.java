package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.event.api.ItemBreakEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ExeDeadrattleMixin {

    @Inject(
            method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"),
            cancellable = true
    )
    public void WhenItemBreak(
            int amount,
            LivingEntity entity,
            Consumer<LivingEntity> breakCallback,
            CallbackInfo ci
    ) {
        ActionResult result = ItemBreakEvent.EVENT.invoker().interact(
                (ItemStack) (Object) this, entity, breakCallback);
        if (result == ActionResult.PASS) ci.cancel();
    }
}
