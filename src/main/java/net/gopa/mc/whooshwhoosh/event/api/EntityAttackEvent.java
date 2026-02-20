package net.gopa.mc.whooshwhoosh.event.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;

public interface EntityAttackEvent {
    Event<PlayerCriticalHitEvent> EVENT = EventFactory.createArrayBacked(PlayerCriticalHitEvent.class,
            listeners -> (target, attacker) -> {
                for (PlayerCriticalHitEvent event : listeners) {
                    ActionResult result = event.interact(target, attacker);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.SUCCESS;
            });

    ActionResult interact(LivingEntity target, LivingEntity attacker);
}
