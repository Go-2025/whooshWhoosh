package net.gopa.mc.whooshwhoosh.registry;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.enchantment.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public enum EnchantmentsRegistry {

    // basic attributes

    FINISHER("finisher", FinisherEnchantment::new),

    // mechanisms

    APOTHEOSIS("apotheosis", ApotheosisEnchantment::new),
    LAUNCH("launch", LaunchEnchantment::new),
    SYMBIOSIS("symbiosis",SymbiosisEnchantment::new),
    ENVENOM("envenom", EnvenomEnchantment::new),
    CATALYSIS("catalysis", CatalysisEnchantment::new),
    WEAKEN_STRIKE("weaken_strike", WeakenStrikeEnchantment::new),
    BONE_SHATTER("bone_shatter", BoneShatterEnchantment::new),
//    MONARCH_WINGS("monarch_wings", MonarchWingsEnchantment::new),
    VAULT("vault", VaultEnchantment::new),
    SPELLSHOT("spellshot", SpellshotEnchantment::new),
    MARK("mark", MarkEnchantment::new),

    // curse

    OFFERING("offering", OfferingEnchantment::new),
    ;

    private final String id;
    private final Supplier<? extends Enchantment> enchantmentSupplier;
    private Enchantment enchantment;

    EnchantmentsRegistry(String id, Supplier<? extends Enchantment> enchantmentSupplier) {
        this.id = id;
        this.enchantmentSupplier = enchantmentSupplier;
    }

    public static void registerAll() {
        for (EnchantmentsRegistry value : values()) {
            Registry.register(Registries.ENCHANTMENT, new Identifier(WhooshwhooshMod.MOD_ID, value.id), value.get());
        }
    }

    public Enchantment get() {
        if (enchantment == null)
            enchantment = enchantmentSupplier.get();
        return enchantment;
    }
}
