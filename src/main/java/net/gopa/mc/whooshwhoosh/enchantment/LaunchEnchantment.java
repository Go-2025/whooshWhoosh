package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.Vec3d;

public class LaunchEnchantment extends Enchantment implements Triggerable {
    public LaunchEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public void onTargetDamage(int level, LivingEntity target, Entity attacker, DamageSource damageSource) {
        WhooshwhooshMod.LOGGER.info("Launch");
        Vec3d direction = target.getVelocity();
        double maxStrength = 0.4f + level * 0.1f;
        double strength = Math.min(Math.abs(direction.y), maxStrength)
                * (1f + level * 0.2f);
        target.setVelocity(0, strength, 0);
        target.setVelocity(0, strength, 0);
//        setVelocity(direction.x, direction.y + 2, direction.z);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof SwordItem;
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
