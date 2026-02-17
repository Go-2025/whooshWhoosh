package net.gopa.mc.whooshwhoosh.event.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public interface EntityDamageEvent {
    Event<EntityDamageEvent> EVENT = EventFactory.createArrayBacked(EntityDamageEvent.class,
            listeners -> (target, attacker, damageSource) -> {
                for (EntityDamageEvent event : listeners) {
                    ActionResult result = event.interact(target, attacker, damageSource);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(LivingEntity target, LivingEntity attacker, DamageSource damageSource);
}
