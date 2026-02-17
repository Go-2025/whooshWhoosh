package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

@Trigger(TriggerPoint.ON_ARROW_HIT)
public class CatalysisEnchantment extends Enchantment implements Triggerable {

    public CatalysisEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public ActionResult onArrowHit(int level, ArrowEntity arrow, LivingEntity target) {
        if (canTrigger(level)) {
            for (StatusEffectInstance effect : target.getStatusEffects()) {
                int oldDuration = effect.getDuration();
                int oldAmplifier = effect.getAmplifier();
                int newDuration = oldDuration / 3;
                int newAmplifier = oldAmplifier == 0 ? 1 : Math.min(oldAmplifier * 2, 225);
                StatusEffectInstance newEffect = new StatusEffectInstance(
                        effect.getEffectType(), newDuration, newAmplifier);
                target.setStatusEffect(newEffect, arrow.getOwner());
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof CrossbowItem;
    }
}
