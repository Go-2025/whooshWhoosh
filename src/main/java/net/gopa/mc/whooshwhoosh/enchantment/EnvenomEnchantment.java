package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.enchantment.abstractclass.CooldownEnchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

import java.util.Optional;

public class EnvenomEnchantment extends CooldownEnchantment {
    public EnvenomEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public void executeCooldownEffect(LivingEntity user, LivingEntity target, int level) {
        StatusEffectInstance existingEffect = target.getStatusEffect(StatusEffects.POISON);

        int newDuration = level * 30 + Optional.ofNullable(existingEffect)
                .map(StatusEffectInstance::getDuration)
                .orElse(0);
        int newAmplifier = Optional.ofNullable(existingEffect)
                .map(StatusEffectInstance::getAmplifier)
                .orElse(0);

        target.addStatusEffect(new StatusEffectInstance(
                StatusEffects.POISON, newDuration, newAmplifier
        ));
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
    public int getMaxCooldown(int level) {
        return 5;
    }
}
