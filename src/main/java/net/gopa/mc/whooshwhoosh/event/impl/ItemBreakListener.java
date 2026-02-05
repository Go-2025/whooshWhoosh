package net.gopa.mc.whooshwhoosh.event.impl;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.DeadRattle;
import net.gopa.mc.whooshwhoosh.event.api.ItemBreakEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.ActionResult;

import java.util.function.Consumer;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.getEnchByNbt;

public class ItemBreakListener implements ItemBreakEvent {

    public static final ItemBreakListener INSTANCE = new ItemBreakListener();

    private ItemBreakListener() {}

    @Override
    public ActionResult interact(ItemStack stack, LivingEntity user, Consumer<LivingEntity> breakCallback) {
        NbtList enchantments = stack.getEnchantments();
        for (NbtElement value : enchantments) {
            Enchantment ench = getEnchByNbt(value);
            if (!(ench instanceof DeadRattle deadRattle)) continue;
            deadRattle.onBreak(stack, user, breakCallback);
        }
        return ActionResult.SUCCESS;
    }
}
