package net.gopa.mc.whooshwhoosh.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class VaultEnchantment extends Enchantment {
    public VaultEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }

    public static void onOffSneak(int level, PlayerEntity self) {
        int duration = 10;
        int amplifier = level * 2;
        self.addStatusEffect(new StatusEffectInstance(
                StatusEffects.JUMP_BOOST, duration, amplifier, false, false, false));
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
