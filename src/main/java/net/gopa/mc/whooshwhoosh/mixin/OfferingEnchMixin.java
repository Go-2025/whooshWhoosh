package net.gopa.mc.whooshwhoosh.mixin;

import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.hasEnch;

@Mixin(ItemStack.class)
public abstract class OfferingEnchMixin {

    @Unique
    private static final Enchantment OFFERING_ENCH = EnchantmentsRegistry.OFFERING.get();

    @ModifyVariable(
            method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private int addRandToDamageWhenWornOut(int amount) {
        ItemStack stack = (ItemStack) (Object) this;

        if (!hasEnch(stack.getEnchantments(), OFFERING_ENCH)) return amount;

        int maxDamage = stack.getMaxDamage();
        Float2IntFunction getIntDamage = (float n) -> (int) Math.max(2f, maxDamage * n);
        int min = getIntDamage.get(0.12f);
        int max = getIntDamage.get(0.20f);

        return amount * WhooshwhooshMod.RANDOM.nextInt(max - min) + min;
    }
}
