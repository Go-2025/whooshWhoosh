package net.gopa.mc.whooshwhoosh.event.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public interface PlayerCriticalHitEvent {
    Event<PlayerCriticalHitEvent> EVENT = EventFactory.createArrayBacked(PlayerCriticalHitEvent.class,
            listeners -> (target, attacker) -> {
                for (PlayerCriticalHitEvent event : listeners) {
                    ActionResult result = event.interact(target, attacker);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(LivingEntity target, LivingEntity attacker);
}
