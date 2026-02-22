package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.toolkit.trigger.Trigger;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Triggerable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

@Trigger(TriggerPoint.ON_ATTACK)
public class LaunchEnchantment extends Enchantment implements Triggerable {
    public LaunchEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public ActionResult onAttack(int level, LivingEntity source, LivingEntity target) {
        if (canTrigger(level)) {
            Vec3d vec3d = target.getVelocity();
            double maxStrength = 0.4f + level * 0.1f;
            double strength = Math.min(Math.abs(vec3d.y), maxStrength) * (1f + level * 0.2f);
            target.setVelocity(vec3d.x / 2f, strength, vec3d.z / 2f);
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
