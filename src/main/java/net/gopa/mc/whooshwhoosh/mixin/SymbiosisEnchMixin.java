package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.event.impl.ItemDamageListener;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.getEnchItems;
import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.hasThisEnch;


@Mixin(ItemStack.class)
public abstract class SymbiosisEnchMixin {

    @Unique
    private static final Enchantment SYMBIOSIS_ENCH = EnchantmentsRegistry.SYMBIOSIS.get();

    @Inject(
            method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z"),
            cancellable = true,
            order = Integer.MAX_VALUE
    )
    private void WhenSymbiosisEnchItemIsDamaged(
            int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback, CallbackInfo ci
    ) {
        if (!canTrigger(entity)) return;

        ServerPlayerEntity player = (ServerPlayerEntity) entity;
        List<ItemStack> symItems = getSymItems(player);
        symItems.sort(Comparator.comparingInt(ItemStack::getDamage));

        damage(symItems, player, amount, symItems.size(), ci);
    }


    @Unique
    private void damage(
            List<ItemStack> symItems,
            ServerPlayerEntity player,
            int amount,
            int destroyedCount,
            CallbackInfo ci
    ) {
        for (ItemStack stack : symItems) {
            int maxDamage = stack.getMaxDamage();
            int tolDamage = maxDamage - stack.getDamage();
            int newDamage = Math.min(tolDamage, amount);
            amount -= newDamage;

            if (!stack.damage(newDamage, player.getRandom(), player)) destroyedCount--;
            if (tolDamage <= 0) break;
        }
        if (!(amount > 0 && destroyedCount == symItems.size())) {
            ci.cancel();
        }
    }

    @Unique
    private boolean canTrigger(LivingEntity entity) {
        if (entity instanceof ServerPlayerEntity) {
            NbtList enchantments = ((ItemStack) (Object) this).getEnchantments();
            return hasThisEnch(enchantments, SYMBIOSIS_ENCH);
        }
        return false;
    }

    @Unique
    private static List<ItemStack> getSymItems(ServerPlayerEntity player) {
        return getEnchItems(player.getInventory(), SYMBIOSIS_ENCH, true);
    }
}