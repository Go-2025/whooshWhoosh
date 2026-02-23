package net.gopa.mc.whooshwhoosh.toolkit.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class EnchantmentUtil {

    public static String getEnchId(Enchantment ench) {
        return Objects.requireNonNull(Registries.ENCHANTMENT.getId(ench), "Unknown enchantment").toString();
    }

    public static boolean hasEnch(NbtList enchNbtList, Enchantment targetEnch) {
        if (enchNbtList == null) return false;
        return enchNbtList.stream()
                .anyMatch(nbt -> {
                    if (!(nbt instanceof NbtCompound compound)) return false;
                    String id = compound.getString("id");
                    return id.equals(Objects.requireNonNull(Registries.ENCHANTMENT.getId(targetEnch)).toString());
                });
    }

    @Nullable
    public static NbtCompound getEnchNbt(NbtList enchNbtList, Enchantment targetEnch) {
        return getEnchNbt(enchNbtList, nbt -> nbt.getString("id").equals(getEnchId(targetEnch)));
    }

    @Nullable
    public static NbtCompound getEnchNbt(NbtList enchNbtList, Function<NbtCompound, Boolean> predicate) {
        for (NbtElement nbt : enchNbtList) {
            if (!(nbt instanceof NbtCompound compound)) continue;
            if (predicate.apply(compound)) {
                return compound;
            }
        }
        return null;
    }

    public static List<ItemStack> getEnchItems(PlayerInventory inventory, Enchantment ench, boolean isDamageable) {
        List<ItemStack> result = new ArrayList<>();
        int inventorySize = inventory.size();

        for (int i = 0; i < inventorySize; i++) {
            ItemStack stack = inventory.getStack(i);
            if (hasEnch(stack.getEnchantments(), ench) && (!isDamageable || stack.isDamageable())) {
                result.add(stack);
            }
        }
        return result;
    }

    public static List<ItemStack> getEnchItems(PlayerInventory inventory, Enchantment ench) {
        return getEnchItems(inventory, ench, false);
    }

    public static Enchantment getEnchByNbt(NbtCompound nbt) {
        return getEnchById(nbt.getString("id"));
    }

    public static Enchantment getEnchById(String id) {
        return Registries.ENCHANTMENT.get(new Identifier(id));
    }

    public static Enchantment getEnchByClass(Class<?> cls) {
        for (Enchantment ench : Registries.ENCHANTMENT) {
            if (ench.getClass().equals(cls)) {
                return ench;
            }
        }
        return null;
    }
}
