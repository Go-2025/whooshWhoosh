package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

/**
 * by {@link net.gopa.mc.whooshwhoosh.enchantment.ApotheosisEnchantment ApotheosisEnchantment}
 */
@Mixin(EnchantmentHelper.class)
public abstract class ApotheosisEnchMixin {

    @Unique
    private static final Enchantment APOTHEOSIS_ENCHANT = EnchantmentsRegistry.APOTHEOSIS.get();

    @ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true)
    private static Map<Enchantment, Integer> MaxAllEnchWhenEnchantingApoth (
            Map<Enchantment, Integer> enchantments
    ) {
        if (!enchantments.containsKey(APOTHEOSIS_ENCHANT)) return enchantments;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            entry.setValue(Math.max(entry.getValue(), enchantment.getMaxLevel()));
        }
        return enchantments;
    }
}
