package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Cooldown;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Trigger;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Triggerable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;

import java.util.Optional;

@Trigger(TriggerPoint.ON_ATTACK)
public class EnvenomEnchantment extends Enchantment implements Cooldown, Triggerable {
    public EnvenomEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public ActionResult onAttack(int level, LivingEntity source, LivingEntity target) {
        if (canTrigger(source, level)) {
            StatusEffectInstance existingEffect = target.getStatusEffect(StatusEffects.POISON);

            int newDuration = level * 30 + Optional.ofNullable(existingEffect)
                    .map(StatusEffectInstance::getDuration)
                    .orElse(0);
            int newAmplifier = Optional.ofNullable(existingEffect)
                    .map(StatusEffectInstance::getAmplifier)
                    .orElse(0);

            target.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.POISON, newDuration, newAmplifier));
            refreshCD(source);
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getDefaultCooldown(int level) {
        return 5;
    }
}
