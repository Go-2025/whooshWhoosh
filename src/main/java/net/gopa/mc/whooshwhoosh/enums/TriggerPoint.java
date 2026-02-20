package net.gopa.mc.whooshwhoosh.enums;

import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;

import java.util.Arrays;


public enum TriggerPoint {

    // 在玩家攻击时触发
    ON_ATTACK,

    // 在生物攻击目标时触发
    ON_ENTITY_DAMAGE,

    // 在暴击时触发
    ON_CRITICAL_HIT,

    // 在物品耐久消耗时触发
    ON_ITEM_DAMAGE,

    // 在物品损坏时触发
    ON_ITEM_BREAK,

    ON_ARROW_HIT,

    ON_JUMP,

    /**
     * @apiNote 仅起占位作用，不做处理
     */
    OTHER();

    public boolean hasTriggerPoint(Class<?> cls) {
        Trigger triggerAnno = cls.getAnnotation(Trigger.class);
        return triggerAnno != null && Arrays.asList(triggerAnno.value()).contains(this);
    }

    public boolean hasTriggerPoint(Triggerable triggerable) {
        return this.hasTriggerPoint(triggerable.getClass());
    }

    public boolean canTrigger(Object obj) {
        return obj instanceof Triggerable triggerableEnch && this.hasTriggerPoint(triggerableEnch);
    }
}
