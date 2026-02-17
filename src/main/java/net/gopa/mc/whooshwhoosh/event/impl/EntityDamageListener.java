package net.gopa.mc.whooshwhoosh.event.impl;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.event.api.EntityDamageEvent;
import net.gopa.mc.whooshwhoosh.util.IteratorUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.List;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.processEnch;

public class EntityDamageListener implements EntityDamageEvent {

    public static final EntityDamageListener INSTANCE = new EntityDamageListener();

    private EntityDamageListener() {
    }

    @Override
    public ActionResult interact(LivingEntity target, LivingEntity attacker, DamageSource damageSource) {
        List<ItemStack> stackList = IteratorUtil.toList(attacker.getArmorItems().iterator());
        stackList.add(attacker.getMainHandStack());
        return processEnch(stackList, (ench, lvl) -> {
            if (ench instanceof Triggerable triggerable && TriggerPoint.ON_TARGET_DAMAGE.hasTriggerPoint(triggerable)) {
                return triggerable.onTargetDamage(lvl, target, attacker, damageSource);
            }
            return ActionResult.PASS;
        });
    }
}
