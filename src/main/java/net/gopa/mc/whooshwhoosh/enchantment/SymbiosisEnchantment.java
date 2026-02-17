package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

import java.util.List;
import java.util.function.Consumer;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.*;
import static net.gopa.mc.whooshwhoosh.util.ItemStackUtil.destroyItem;

@Trigger(TriggerPoint.ON_ITEM_BREAK)
public class SymbiosisEnchantment extends Enchantment implements Triggerable {

    public SymbiosisEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.BREAKABLE, EquipmentSlot.values());
    }

//    @Override
//    public void onItemDamage(int level, ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
//        if (!hasThisEnch(stack.getEnchantments(), this)) return;
//
//        ServerPlayerEntity player = (ServerPlayerEntity) entity;
//        List<ItemStack> symItems = getEnchItems(
//                player.getInventory(), EnchantmentsRegistry.SYMBIOSIS.get(), true);
//        symItems.sort(Comparator.comparingInt(ItemStack::getDamage));
//
//        int destroyedCount = symItems.size();
//        for (ItemStack s : symItems) {
//            int maxDamage = s.getMaxDamage();
//            int tolDamage = maxDamage - s.getDamage();
//            int newDamage = Math.min(tolDamage, amount);
//            amount -= newDamage;
//
//            if (!s.damage(newDamage, player.getRandom(), player)) destroyedCount--;
//            if (tolDamage <= 0) break;
//        }
//        if (!(amount > 0 && destroyedCount == symItems.size())) {
//            return;
//        }
//    }


    @Override
    public ActionResult onItemBreak(int level, ItemStack stack, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
        if (canTrigger(level) && entity instanceof ServerPlayerEntity player) {
            Enchantment SYMBIOSIS_ENCH = EnchantmentsRegistry.SYMBIOSIS.get();
            PlayerInventory inventory = player.getInventory();
            List<ItemStack> symItems = getEnchItems(inventory, SYMBIOSIS_ENCH, true);

            symItems.removeIf(item -> {
                NbtList enchantments = item.getEnchantments();
                if (item.equals(stack) || enchantments == null) return false;

                boolean processed = false;
                for (NbtElement value : enchantments) {
                    Enchantment ench = getEnchByNbt((NbtCompound) value);
                    if (!(ench instanceof SymbiosisEnchantment)
                            && ench instanceof Triggerable triggerableEnch
                            && TriggerPoint.ON_ITEM_BREAK.hasTriggerPoint(triggerableEnch)
                    ) {
                        WhooshwhooshMod.LOGGER.info("Processing Symbiosis enchantment");
                        triggerableEnch.onItemBreak(level, item, entity, breakCallback);
                        processed = true;
                    }
                }

                if (processed) {
                    destroyItem(item, player, breakCallback);
                    return true;
                }
                return false;
            });
        }
        return ActionResult.PASS;
    }
}
