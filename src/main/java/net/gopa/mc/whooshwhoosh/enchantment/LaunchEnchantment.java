package net.gopa.mc.whooshwhoosh.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class LaunchEnchantment extends ModEnchantment {
    public LaunchEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity livingTarget) {
            float strength = 1f + level * 0.24f;
            Vec3d direction = livingTarget.getVelocity();
            livingTarget.setVelocity(
                    direction.x, direction.y * strength, direction.z
            );
        }
        super.onTargetDamaged(user, target, level);
    }

    @Override
    public int getMinPower(int level) {
        return 4 + level * 12;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 36;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
