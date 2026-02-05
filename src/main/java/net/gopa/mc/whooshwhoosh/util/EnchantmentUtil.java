package net.gopa.mc.whooshwhoosh.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class EnchantmentUtil {

    public static boolean hasThisEnch(NbtList enchNbtList, Enchantment targetEnch) {
        if (enchNbtList == null || enchNbtList.isEmpty()) return false;
        return enchNbtList.stream()
                .anyMatch(nbt -> {
                    if (!(nbt instanceof NbtCompound compound)) return false;
                    String id = compound.getString("id");
                    return id.equals(Objects.requireNonNull(Registries.ENCHANTMENT.getId(targetEnch)).toString());
                });
    }

    public static List<ItemStack> getEnchItems(PlayerInventory inventory, Enchantment ench, boolean isDamageable) {
        List<ItemStack> result = new ArrayList<>();
        int inventorySize = inventory.size();

        for (int i = 0; i < inventorySize; i++) {
            ItemStack stack = inventory.getStack(i);
            if (hasThisEnch(stack.getEnchantments(), ench) && (!isDamageable || stack.isDamageable())) {
                result.add(stack);
            }
        }
        return result;
    }

    public static List<ItemStack> getEnchItems(PlayerInventory inventory, Enchantment ench) {
        return getEnchItems(inventory, ench, false);
    }

    public static Enchantment getEnchByNbt(NbtElement nbt) {
        if (!(nbt instanceof NbtCompound compound)) return null;
        String enchId = compound.getString("id");
        return Registries.ENCHANTMENT.get(new Identifier(enchId));
    }


}
