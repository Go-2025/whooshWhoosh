package net.gopa.mc.whooshwhoosh.event.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;

public interface EntityJumpEvent {
    Event<EntityJumpEvent> EVENT = EventFactory.createArrayBacked(EntityJumpEvent.class,
            listeners -> (self) -> {
                for (EntityJumpEvent event : listeners) {
                    ActionResult result = event.interact(self);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.SUCCESS;
            });

    ActionResult interact(LivingEntity self);
}
