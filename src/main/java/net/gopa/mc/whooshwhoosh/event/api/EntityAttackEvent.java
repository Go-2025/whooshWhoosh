package net.gopa.mc.whooshwhoosh.event.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public interface EntityAttackEvent {
    Event<EntityAttackEvent> EVENT = EventFactory.createArrayBacked(EntityAttackEvent.class,
            listeners -> (target, attacker, damageSource) -> {
                for (EntityAttackEvent event : listeners) {
                    ActionResult result = event.interact(target, attacker, damageSource);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(LivingEntity target, LivingEntity attacker, DamageSource damageSource);
}
