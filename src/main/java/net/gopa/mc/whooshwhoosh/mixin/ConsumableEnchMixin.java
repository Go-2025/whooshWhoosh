package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.enchantment.ConsumableEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.processEnchantments;

/**
 * by {@link ConsumableEnchantment ConsumableEnchantment}
 */
@Mixin(EnchantmentHelper.class)
public abstract class ConsumableEnchMixin {

    @Inject(method = "set", at = @At("TAIL"), order = 115)
    private static void whenEnchantingApoth(
            Map<Enchantment, Integer> enchantments,
            ItemStack stack,
            CallbackInfo ci
    ) {
        processEnchantments(stack, (ench, compound) -> {
            if (ench instanceof ConsumableEnchantment) stack.getEnchantments().remove(compound);});
    }
}
