package net.gopa.mc.whooshwhoosh.toolkit.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;

import java.util.function.Consumer;

public final class ItemStackUtil {

    public static void damageAndBreakItem(
            int amount, ItemStack stack, ServerPlayerEntity player, Consumer<LivingEntity> breakCallback
    ) {
        if (stack.damage(amount, player.getRandom(), player)) {
            stack.damage(1, player, (e) -> destroyItem(stack, player, breakCallback));
        }
    }

    public static void destroyItem(
            ItemStack stack, ServerPlayerEntity player, Consumer<LivingEntity> breakCallback
    ) {
        breakCallback.accept(player);
        stack.decrement(1);
        player.incrementStat(Stats.BROKEN.getOrCreateStat(stack.getItem()));
        stack.setDamage(0);
    }
}
