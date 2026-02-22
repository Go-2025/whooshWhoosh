package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Consumable;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Trigger;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.gopa.mc.whooshwhoosh.util.EnchantmentUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.ActionResult;

import java.util.Map;

@Trigger(value = TriggerPoint.ON_ITEM_ENCHANT)
public class ApotheosisEnchantment extends Enchantment implements Consumable {

    public ApotheosisEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.VANISHABLE, EquipmentSlot.values());
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof Item;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public ActionResult consume(int level, ItemStack source, Map<Enchantment, Integer> enchantments) {
        NbtList enchantList = source.getEnchantments();

        for (int i = enchantList.size() - 1; i >= 0; i--) {
            NbtCompound nbt = enchantList.getCompound(i);
            String enchId = nbt.getString("id");
            Enchantment ench = EnchantmentUtil.getEnchById(enchId);

            if (ench != null) setMaxLevel(ench, nbt);
        }
        return ActionResult.PASS;
    }

    private void setMaxLevel(Enchantment ench, NbtCompound nbt) {
        nbt.putInt("lvl", Math.max(nbt.getInt("lvl"), ench.getMaxLevel()));
    }
}
