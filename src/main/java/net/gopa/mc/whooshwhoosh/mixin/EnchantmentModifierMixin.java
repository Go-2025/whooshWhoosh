package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.enchantment.ModEnchantment;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(EnchantmentHelper.class)
public class EnchantmentModifierMixin {

    private static final Enchantment APOTHEOSIS_ENCHANT = EnchantmentsRegistry.APOTHEOSIS.get();
    private static final Registry<Enchantment> ENCHANTMENT_REGISTRY = Registries.ENCHANTMENT;

    private static void processEnchantments(
            ItemStack stack,
            BiConsumer<Enchantment, NbtCompound> processor
    ) {
        NbtList enchantList = stack.getEnchantments();

        for (int i = enchantList.size() - 1; i >= 0; i--) {
            NbtCompound compound = enchantList.getCompound(i);
            String enchId = compound.getString("id");
            Enchantment ench = ENCHANTMENT_REGISTRY.get(new Identifier(enchId));

            if (ench != null) processor.accept(ench, compound);
        }
    }

    @Inject(method = "set", at = @At("TAIL"), order = 115)
    private static void removeIsConsumableEnch(
            Map<Enchantment, Integer> enchantments,
            ItemStack stack,
            CallbackInfo ci
    ) {
        processEnchantments(stack, (ench, compound) -> {
            if (ModEnchantment.getIsConsumable(ench)) stack.getEnchantments().remove(compound);});
    }

    @Inject(method = "set", at = @At("TAIL"), order = 114)
    private static void SetTheEnchLevelToTheMaxWhenEnchantingApoth (
            Map<Enchantment, Integer> enchantments,
            ItemStack stack,
            CallbackInfo ci
    ) {
        if (!enchantments.containsKey(APOTHEOSIS_ENCHANT)) return;
        processEnchantments(stack, (ench, compound) ->
            compound.putShort("lvl", (short) Math.max(compound.getInt("lvl"), ench.getMaxLevel())));
    }
}
