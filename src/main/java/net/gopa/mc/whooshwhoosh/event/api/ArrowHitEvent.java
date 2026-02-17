package net.gopa.mc.whooshwhoosh.event.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.ActionResult;

public interface ArrowHitEvent {
    Event<ArrowHitEvent> EVENT = EventFactory.createArrayBacked(ArrowHitEvent.class,
            listeners -> (arrow, target) -> {
                for (ArrowHitEvent event : listeners) {
                    ActionResult result = event.interact(arrow, target);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(ArrowEntity arrow, LivingEntity target);
}
