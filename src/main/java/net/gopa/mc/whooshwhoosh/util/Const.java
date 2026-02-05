package net.gopa.mc.whooshwhoosh.util;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;

public final class Const {

    public static String getRootDataKey() {
        return WhooshwhooshMod.MOD_ID + "_data";
    }

    public static String getDataKey(String key, Enchantment ench) {
        return Registries.ENCHANTMENT.getId(ench) + "_" + key;
    }
}
