package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.getEnchItems;
import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.hasEnchantment;


@Mixin(ItemStack.class)
public abstract class SymbiosisEnchMixin {

    @Unique
    private static final Enchantment SYMBIOSIS_ENCH = EnchantmentsRegistry.SYMBIOSIS.get();

    @Inject(
            method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void WhenSymbiosisEnchItemIsDamaged(
            int amount, Random rand, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir
    ) {
        ItemStack mainStack = (ItemStack) (Object) this;
        if (!hasEnchantment(mainStack.getEnchantments(), SYMBIOSIS_ENCH)) return;

        PlayerInventory inventory = player.getInventory();
        List<ItemStack> symItems = getEnchItems(inventory, SYMBIOSIS_ENCH);
        if (symItems.isEmpty()) return;

        if (symItems.stream().allMatch(stack -> stack.getDamage() >= stack.getMaxDamage())) {
            for (ItemStack stack : symItems) stack.decrement(1);
            return;
        }

        ItemStack minDamageStack = symItems.get(0);
        for (ItemStack stack : symItems)
            if (stack.getDamage() < minDamageStack.getDamage()) minDamageStack = stack;
        minDamageStack.setDamage(
                Math.min(minDamageStack.getDamage() + amount, minDamageStack.getMaxDamage()));

        cir.cancel();
    }
}