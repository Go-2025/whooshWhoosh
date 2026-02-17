package net.gopa.mc.whooshwhoosh.event.impl;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.event.api.ArrowHitEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.processEnch;

public class ArrowHitListener implements ArrowHitEvent {

    public static final ArrowHitListener INSTANCE = new ArrowHitListener();
    @Override
    public ActionResult interact(ArrowEntity arrow, LivingEntity target) {
        if (arrow.getOwner() instanceof LivingEntity attack) {
            ItemStack stack = attack.getMainHandStack();
            return processEnch(stack, (ench, lvl) -> {
                if (ench instanceof Triggerable triggerable && TriggerPoint.ON_ARROW_HIT.hasTriggerPoint(triggerable))
                    return triggerable.onArrowHit(lvl, arrow, target);
                return ActionResult.PASS;
            });
        }
        return ActionResult.PASS;
    }
}
