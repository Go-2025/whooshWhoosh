package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Cooldown;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Trigger;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Triggerable;
import net.gopa.mc.whooshwhoosh.util.EntityUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.ActionResult;

@Trigger(TriggerPoint.ON_ARROW_HIT)
public class SpellshotEnchantment extends Enchantment implements Cooldown, Triggerable {
    public SpellshotEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public ActionResult onArrowHit(int level, LivingEntity target, ArrowEntity arrow) {
        Entity entity = arrow.getOwner();
        if (canTrigger(entity, level)) {
            boolean triggered = false;
            Runnable addEffect = () -> arrow.addEffect(new StatusEffectInstance(
                    StatusEffects.INSTANT_DAMAGE, 2, level - 1));
            if (entity instanceof PlayerEntity player) {
                if (EntityUtils.getLastBowPullProgress(player) > 0.9f) {
                    addEffect.run();
                    triggered = true;
                }
            } else {
                addEffect.run();
                triggered = true;
            }
            if (triggered) refreshCD(entity);
        }
        return ActionResult.PASS;
    }

    @Override
    public int getDefaultCooldown(int level) {
        return 10;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
