package net.gopa.mc.whooshwhoosh.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class EnchantmentUtil {

    public static boolean hasEnchantment(NbtList enchNbtList, Enchantment targetEnch) {
        if (enchNbtList == null || enchNbtList.isEmpty()) return false;
        return enchNbtList.stream()
                .anyMatch(nbt -> {
                    if (nbt instanceof NbtCompound compound) {
                        String id = compound.getString("id");
//                        WhooshwhooshMod.LOGGER.info("Ench ID: {}", Objects.requireNonNull(Registries.ENCHANTMENT.getId(targetEnch)));
                        return id.equals(Objects.requireNonNull(Registries.ENCHANTMENT.getId(targetEnch)).toString());
                    } return false;
                });
    }

    public static List<ItemStack> getEnchItems(PlayerInventory inventory, Enchantment ench) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (hasEnchantment(stack.getEnchantments(), ench)) items.add(stack);
        }
        return items;
    }
}
