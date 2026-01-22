package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.enchantment.ConsumableEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

/**
 * by {@link ConsumableEnchantment ConsumableEnchantment}
 */
@Mixin(EnchantmentHelper.class)
public abstract class ConsumableEnchMixin {

    @ModifyVariable(method = "set", at = @At("HEAD"), order = Integer.MAX_VALUE, argsOnly = true)
    private static Map<Enchantment, Integer> removeWhenEnchanting(
            Map<Enchantment, Integer> values
    ) {
        for (Map.Entry<Enchantment, Integer> entry : values.entrySet()) {
            Enchantment ench = entry.getKey();
            if (ench instanceof ConsumableEnchantment) values.remove(ench);
        }
        return values;
    }
}
