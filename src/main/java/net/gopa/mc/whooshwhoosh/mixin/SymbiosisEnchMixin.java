package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
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
import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.hasEnchantment;
import static net.gopa.mc.whooshwhoosh.util.ItemStackUtil.destroyItem;


@Mixin(ItemStack.class)
public abstract class SymbiosisEnchMixin {

    @Unique
    private static final Enchantment SYMBIOSIS_ENCH = EnchantmentsRegistry.SYMBIOSIS.get();

    @Unique
    private static List<ItemStack> getSymItems(ItemStack mainStack, ServerPlayerEntity player) {
        List<ItemStack> enchItems = new ArrayList<>();
        if (!hasEnchantment(mainStack.getEnchantments(), SYMBIOSIS_ENCH)) return enchItems;

        PlayerInventory inventory = player.getInventory();
        enchItems = getEnchItems(inventory, SYMBIOSIS_ENCH);

        return enchItems;
    }

    @Inject(
            method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z"),
            cancellable = true
    )
    private void WhenSymbiosisEnchItemIsDamaged(
            int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback, CallbackInfo ci
    ) {
        ItemStack mainStack = (ItemStack) (Object) this;
        if (!(entity instanceof ServerPlayerEntity player)) return;
        List<ItemStack> symItems = getSymItems(mainStack, player);

        if (symItems.isEmpty()) return;

//        Optional<ItemStack> minDamageStackOpt = symItems.stream()
//                .min(Comparator.comparingInt(ItemStack::getDamage));
//
//        if (minDamageStackOpt.isEmpty()) return;
//
//        boolean allDamaged = symItems.stream()
//                .allMatch(stack -> stack.getDamage() >= stack.getMaxDamage());
//        if (allDamaged) {
//            symItems.forEach(stack -> destroyItem(stack, player, breakCallback));
//        } else {
//            ItemStack minDamageStack = minDamageStackOpt.get();
//            int newDamage = Math.min(minDamageStack.getDamage() + amount, minDamageStack.getMaxDamage());
//
//        }

        symItems.sort(Comparator.comparingInt(ItemStack::getDamage));
        Iterator<ItemStack> stackIter = symItems.iterator();
        int destroyedCount = symItems.size();
        int i = 0;
        while (stackIter.hasNext()) {
            ItemStack stack = stackIter.next();
            int maxDamage = stack.getMaxDamage();
            int tolDamage = maxDamage - stack.getDamage();
            int newDamage = Math.min(tolDamage, amount);
            amount -= newDamage;

            if (!stack.damage(newDamage, player.getRandom(), player)) destroyedCount--;
            if (tolDamage <= 0) break;
        }
        if (destroyedCount == symItems.size()) {
            symItems.forEach(stack -> destroyItem(stack, player, breakCallback));
        }
        ci.cancel();
    }
}