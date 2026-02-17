package net.gopa.mc.whooshwhoosh.event.impl;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.event.api.ItemBreakEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.function.Consumer;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.processEnch;

public class ItemBreakListener implements ItemBreakEvent {

    public static final ItemBreakListener INSTANCE = new ItemBreakListener();

    private ItemBreakListener() {}

    @Override
    public ActionResult interact(ItemStack stack, LivingEntity user, Consumer<LivingEntity> breakCallback) {
        return processEnch(stack, (ench, lvl) -> {
            if (ench instanceof Triggerable triggerableEnch && TriggerPoint.ON_ITEM_BREAK.hasTriggerPoint(triggerableEnch)) {
                return triggerableEnch.onItemBreak(lvl, stack, user, breakCallback);
            }
            return ActionResult.PASS;
        });
    }
}
