package net.gopa.mc.whooshwhoosh.enums;

import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.event.impl.*;
import net.minecraft.enchantment.Enchantment;

import java.util.Arrays;

import static net.gopa.mc.whooshwhoosh.util.AnnotationUtil.getClassAnnotation;

public enum TriggerPoint {
    // 在玩家攻击时触发
    ON_ATTACK(),

    // 在生物攻击目标时触发
    ON_TARGET_DAMAGE(EntityDamageListener.class),

    // 在暴击时触发
    ON_CRITICAL_HIT(PlayerCriticalHitListener.class),

    // 在物品耐久消耗时触发
    ON_ITEM_DAMAGE(ItemDamageListener.class),

    // 在物品损坏时触发
    ON_ITEM_BREAK(ItemBreakListener.class),

    ON_ARROW_HIT(ArrowHitListener.class),

    ON_JUMP(EntityJumpListener.class),
    ;

    private final Class<?>[] listenerClass;

    TriggerPoint(Class<?>[] listenerClass) {
        this.listenerClass = listenerClass;
    }
    TriggerPoint(Class<?> listenerClass) {
        this(new Class<?>[]{listenerClass});
    }
    TriggerPoint() {
        this(new Class<?>[]{});
    }

    public boolean hasTriggerPoint(Triggerable triggerable) {
        Trigger triggerAnno = getClassAnnotation(triggerable.getClass(), Trigger.class);
        return triggerAnno != null &&
//                    Arrays.stream(triggerAnno.point()).noneMatch(point ->
//                    Arrays.asList(point.getListenerClasses()).contains(EntityDamageListener.class))
                Arrays.asList(triggerAnno.value()).contains(this);
    }

    public boolean canTrigger(Enchantment ench) {
        return ench instanceof Triggerable triggerableEnch && this.hasTriggerPoint(triggerableEnch);
    }

    public Class<?>[] getListenerClasses() {
        return listenerClass;
    }
}
