package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

import static net.gopa.mc.whooshwhoosh.util.EntityUtils.isCritical;
import static net.gopa.mc.whooshwhoosh.util.StatusEffectUtils.addStatusEffects;

public class BoneShatterEnchantment extends Enchantment implements Triggerable {
    public BoneShatterEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public void onCriticalHit(int level, LivingEntity target, Entity attacker) {
        int duration = (level + 2) * 20 / 8 * 9;
        int amplifier = 1;
        if (attacker instanceof LivingEntity livingAttacker && isCritical(livingAttacker, target)) {
            addStatusEffects(target, livingAttacker,
                    new StatusEffectInstance(StatusEffects.WEAKNESS, duration, amplifier),
                    new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, amplifier),
                    new StatusEffectInstance(StatusEffects.SLOWNESS, duration, amplifier)
            );
        }
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof AxeItem;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
