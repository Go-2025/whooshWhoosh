package net.gopa.mc.whooshwhoosh.toolkit.registry;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class RegistryEnchantment {

    private static final Set<Enchantment> enchantments = new HashSet<>();

    public static void register(Enchantment ench, String id) {
        enchantments.add(ench);
        Registry.register(Registries.ENCHANTMENT, new Identifier(WhooshwhooshMod.MOD_ID, id), ench);
    }

    public static Set<Enchantment> getEnchantments() {
        return Collections.unmodifiableSet(enchantments);
    }
}
