package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.event.api.ItemEnchantEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public abstract class ItemEnchantMixin {

    @Inject(method = "set", at = @At("TAIL"))
    private static void consume (
            Map<Enchantment, Integer> enchantments,
            ItemStack stack,
            CallbackInfo ci
    ) {
        ActionResult res = ItemEnchantEvent.EVENT.invoker().interact(stack, enchantments);
    }
}
