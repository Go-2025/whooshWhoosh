package net.gopa.mc.whooshwhoosh.event.impl;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.event.api.EntityJumpEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.processEnch;

public class EntityJumpListener implements EntityJumpEvent {

    public static final EntityJumpListener INSTANCE = new EntityJumpListener();

    @Override
    public ActionResult interact(LivingEntity entity) {
//        for (ItemStack stack : entity.getArmorItems()) {
//            processEnch(stack, (ench, lvl) -> {
//                if (ench instanceof Triggerable triggerableEnch && TriggerPoint.ON_JUMP.hasTriggerPoint(triggerableEnch)) {
//                    triggerableEnch.onEntityJump(lvl, entity);
//                }
//            });
//        }

        ItemStack stack = entity.getMainHandStack();
        return processEnch(stack, (ench, lvl) -> {
            if (ench instanceof Triggerable triggerableEnch && TriggerPoint.ON_JUMP.hasTriggerPoint(triggerableEnch)) {
                return triggerableEnch.onEntityJump(lvl, entity);
            }
            return ActionResult.PASS;
        });
    }
}
