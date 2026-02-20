package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.util.PlayerUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;

@Trigger(TriggerPoint.ON_ATTACK)
public class SacrificialStrikeEnchantment extends Enchantment implements Triggerable {

    public SacrificialStrikeEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public ActionResult onAttack(int level, LivingEntity source, LivingEntity target) {
        if (canTrigger(level)) {
            if (!(source instanceof PlayerEntity)) {
                execute(level, source, target);
            }
        }
        return ActionResult.PASS;
    }

    public ActionResult onPlayerAttack(int level, PlayerEntity source, Entity target) {
        if (canTrigger(level)) {
            if (target instanceof LivingEntity livingTarget) {
                float progress = PlayerUtils.getAttackCDProgress(source);
                if (progress >= 0.5) {
                    execute(level, source, livingTarget);
                }
            }
        }
        return ActionResult.PASS;
    }

    private void execute(int level, LivingEntity source, LivingEntity target) {
        source.damage(new DamageSource(source.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(DamageTypes.MAGIC).orElseThrow()), 1);
        target.setHealth(target.getHealth() - level - 1);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof SwordItem || item instanceof AxeItem;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
