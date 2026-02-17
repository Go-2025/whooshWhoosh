package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.util.PlayerUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import static net.gopa.mc.whooshwhoosh.util.StatusEffectUtils.addStatusEffects;

@Trigger(TriggerPoint.ON_CRITICAL_HIT)
public class BoneShatterEnchantment extends Enchantment implements Triggerable {
    public BoneShatterEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public ActionResult onCriticalHit(int level, LivingEntity target, Entity attacker) {
        if (canTrigger(level)) {
            int duration = (level + 2) * 20 / 8 * 9;
            int amplifier = 1;
            if (attacker instanceof LivingEntity livingAttacker && PlayerUtils.isCritical(livingAttacker, target)) {
                addStatusEffects(target, livingAttacker,
                        new StatusEffectInstance(StatusEffects.WEAKNESS, duration, amplifier),
                        new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, amplifier),
                        new StatusEffectInstance(StatusEffects.SLOWNESS, duration, amplifier)
                );
            }
        }
        return ActionResult.PASS;
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
