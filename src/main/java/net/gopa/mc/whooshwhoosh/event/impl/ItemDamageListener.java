package net.gopa.mc.whooshwhoosh.event.impl;

import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
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
import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.processEnch;

public class ItemDamageListener implements ItemDamageEvent {

    public static final ItemDamageListener INSTANCE = new ItemDamageListener();

    private ItemDamageListener() {}

    @Override
    public ActionResult interact(ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
        return processEnch(stack, (ench, lvl) -> {
            if (ench instanceof Triggerable triggerableEnch && TriggerPoint.ON_ITEM_DAMAGE.hasTriggerPoint(triggerableEnch)) {
                return triggerableEnch.onItemDamage(lvl, stack, amount, entity, breakCallback);
            }
            return ActionResult.PASS;
        });
    }
}
