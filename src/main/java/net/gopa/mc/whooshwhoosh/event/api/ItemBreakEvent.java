package net.gopa.mc.whooshwhoosh.event.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.function.Consumer;

public interface ItemBreakEvent {
    Event<ItemBreakEvent> EVENT = EventFactory.createArrayBacked(ItemBreakEvent.class,
            listeners -> (stack, entity, breakCallback) -> {
                for (ItemBreakEvent event : listeners) {
                    ActionResult result = event.interact(stack, entity, breakCallback);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(ItemStack stack, LivingEntity user, Consumer<LivingEntity> breakCallback);
}
