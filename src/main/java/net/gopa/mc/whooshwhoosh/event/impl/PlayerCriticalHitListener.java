package net.gopa.mc.whooshwhoosh.event.impl;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.event.api.PlayerCriticalHitEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.processEnch;

public class PlayerCriticalHitListener implements PlayerCriticalHitEvent {

    public static final PlayerCriticalHitEvent INSTANCE = new PlayerCriticalHitListener();

    private PlayerCriticalHitListener() {}

    @Override
    public ActionResult interact(LivingEntity target, LivingEntity attacker) {
        return processEnch(attacker.getMainHandStack(), (ench, lvl) -> {
            if (ench instanceof Triggerable triggerable && TriggerPoint.ON_CRITICAL_HIT.hasTriggerPoint(triggerable)) {
                return triggerable.onCriticalHit(lvl, target, attacker);
            }
            return ActionResult.PASS;
        });
    }
}
