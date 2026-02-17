package net.gopa.mc.whooshwhoosh.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public final class EnchantmentUtil {

    public static boolean hasEnch(NbtList enchNbtList, Enchantment targetEnch) {
        if (enchNbtList == null) return false;
        return enchNbtList.stream()
                .anyMatch(nbt -> {
                    if (!(nbt instanceof NbtCompound compound)) return false;
                    String id = compound.getString("id");
                    return id.equals(Objects.requireNonNull(Registries.ENCHANTMENT.getId(targetEnch)).toString());
                });
    }

    public static NbtCompound getEnchNbt(NbtList enchNbtList, Enchantment targetEnch) {
        for (NbtElement nbt : enchNbtList) {
            if (!(nbt instanceof NbtCompound compound)) continue;
            String id = compound.getString("id");
            if (id.equals(Objects.requireNonNull(Registries.ENCHANTMENT.getId(targetEnch)).toString())) {
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
        try {
            return Registries.ENCHANTMENT.get(new Identifier(nbt.getString("id")));
        } catch (InvalidIdentifierException e) {
            return null;
        }

    }

    public static ActionResult processEnch(ItemStack stack, BiFunction<Enchantment, Integer, ActionResult> processor) {
        NbtList enchNbtList = stack.getEnchantments();
        for (NbtElement v : enchNbtList) {
            NbtCompound nbt = (NbtCompound) v;
            Enchantment ench = getEnchByNbt(nbt);
            ActionResult result = processor.apply(ench, nbt.getInt("lvl"));
            if (!result.equals(ActionResult.PASS)) return result;
        }
        return ActionResult.PASS;
    }

    public static ActionResult processEnch(Iterator<ItemStack> stackIter, BiFunction<Enchantment, Integer, ActionResult> processor) {
        while (stackIter.hasNext()) {
            ItemStack stack = stackIter.next();
            ActionResult result = processEnch(stack, processor);
            if (!result.equals(ActionResult.PASS)) return result;
        }
        return ActionResult.PASS;
    }

    public static ActionResult processEnch(List<ItemStack> stackList, BiFunction<Enchantment, Integer, ActionResult> processor) {
        for (ItemStack stack : stackList) {
            ActionResult result = processEnch(stack, processor);
            if (!result.equals(ActionResult.PASS)) return result;
        }
        return ActionResult.PASS;
    }
}
