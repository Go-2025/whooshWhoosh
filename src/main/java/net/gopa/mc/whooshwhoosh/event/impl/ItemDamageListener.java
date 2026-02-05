package net.gopa.mc.whooshwhoosh.event.impl;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.DeadRattle;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.event.api.ItemDamageEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.ActionResult;

import java.util.function.Consumer;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.getEnchByNbt;

public class ItemDamageListener implements ItemDamageEvent {

    public static final ItemDamageListener INSTANCE = new ItemDamageListener();

    private ItemDamageListener() {}

    @Override
    public ActionResult interact(ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
        NbtList enchantments = stack.getEnchantments();
        for (NbtElement value : enchantments) {
            Enchantment ench = getEnchByNbt(value);
            NbtCompound nbt = (NbtCompound) value;
            if (ench instanceof Triggerable triggerableEnch) {
                int level = nbt.getInt("lvl");
                Triggerable proxy = Triggerable.CheckProxyFactory.createProxy(triggerableEnch, Triggerable.class);
                proxy.onItemDamage(level, stack, amount, entity, breakCallback);
            }
        }
        return ActionResult.SUCCESS;
    }
}
