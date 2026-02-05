package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.minecraft.enchantment.Enchantment;

import java.util.Map;

public interface Consumable {

    void consume(Map<Enchantment, Integer> values, Enchantment self);
}
