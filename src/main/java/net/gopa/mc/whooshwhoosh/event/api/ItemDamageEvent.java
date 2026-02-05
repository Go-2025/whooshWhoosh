package net.gopa.mc.whooshwhoosh.event.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

public interface ItemDamageEvent {
    Event<ItemDamageEvent> EVENT = EventFactory.createArrayBacked(ItemDamageEvent.class,
            listeners -> ((stack, amount, entity, breakCallback) -> {
                for (ItemDamageEvent event : listeners) {
                    ActionResult result = event.interact(stack, amount, entity, breakCallback);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            }));

    ActionResult interact(ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback);
}
