package net.gopa.mc.whooshwhoosh.event.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;

public interface EntityLandEvent {
    Event<EntityLandEvent> EVENT = EventFactory.createArrayBacked(EntityLandEvent.class,
            listeners -> (self) -> {
                for (EntityLandEvent event : listeners) {
                    ActionResult result = event.interact(self);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.SUCCESS;
            });

    ActionResult interact(Entity self);
}
