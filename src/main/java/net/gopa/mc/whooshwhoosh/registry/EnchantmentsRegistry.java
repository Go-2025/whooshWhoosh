package net.gopa.mc.whooshwhoosh.registry;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public enum EnchantmentsRegistry implements ModRegistry<Enchantment> {
    ;

    private final String id;
    private final Supplier<? extends Enchantment> enchantmentSupplier;
    private Enchantment enchantment;

    EnchantmentsRegistry(String id, Supplier<? extends Enchantment> enchantmentSupplier) {
        this.id = id;
        this.enchantmentSupplier = enchantmentSupplier;
    }


    @Override
    public void registerAll() {
        for (EnchantmentsRegistry value : values()) {
            Registry.register(Registries.ENCHANTMENT, new Identifier(WhooshwhooshMod.MOD_ID, value.id), value.get());
        }
    }

    @Override
    public Enchantment get() {
        return null;
    }
}
