package net.gopa.mc.whooshwhoosh.event.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.Map;

public interface ItemEnchantEvent {
    Event<ItemEnchantEvent> EVENT = EventFactory.createArrayBacked(ItemEnchantEvent.class,
            listeners -> (ItemStack source, Map<Enchantment, Integer> enchantments) -> {
                for (ItemEnchantEvent event : listeners) {
                    ActionResult result = event.interact(source, enchantments);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.SUCCESS;
            });

    ActionResult interact(ItemStack source, Map<Enchantment, Integer> enchantments);
}
