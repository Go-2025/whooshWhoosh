package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.minecraft.enchantment.Enchantment;

import java.util.Map;

/**
 * 消耗的，在铁砧或附魔台上附魔时会被消耗
 */
public interface Consumable {

    /**
     *
     * @param values
     * @param self
     */
    void consume(Map<Enchantment, Integer> values, Enchantment self);
}
