package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;

@Trigger(TriggerPoint.ON_TARGET_DAMAGE)
public class WeakenStrikeEnchantment extends Enchantment implements Triggerable {
    public WeakenStrikeEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.WEAPON, EquipmentSlot.values());
    }

    @Override
    public ActionResult onTargetDamage(int level, LivingEntity target, Entity attacker, DamageSource damageSource) {
        if (canTrigger(level)) {
            target.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.WEAKNESS,
                    (int) (1.4 * Math.sqrt(level + 1) * 20),
                    level - 1));
        }
        return ActionResult.PASS;
    }

    @Override
    public double getProbability(int level) {
        return 0.34d;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
