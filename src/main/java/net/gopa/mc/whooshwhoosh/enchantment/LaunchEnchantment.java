package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

@Trigger(TriggerPoint.ON_TARGET_DAMAGE)
public class LaunchEnchantment extends Enchantment implements Triggerable {
    public LaunchEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public ActionResult onTargetDamage(int level, LivingEntity target, Entity attacker, DamageSource damageSource) {
        if (canTrigger(level)) {
            Vec3d direction = target.getVelocity();
            double maxStrength = 0.4f + level * 0.1f;
            double strength = Math.min(Math.abs(direction.y), maxStrength) * (1f + level * 0.2f);
            WhooshwhooshMod.LOGGER.info("{}", target);
            target.setVelocity(0, strength, 0);
            target.setVelocity(0, strength, 0);
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
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
