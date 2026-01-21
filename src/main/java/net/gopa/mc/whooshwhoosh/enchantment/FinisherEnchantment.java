package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class FinisherEnchantment extends Enchantment {

    public FinisherEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity livingTarget) {
            float strength =  (level - 0.5f) / 20f;

            float health = livingTarget.getHealth();
            float maxHealth = livingTarget.getMaxHealth();

//            WhooshwhooshMod.LOGGER.info("{}:{}:{}", level, health / maxHealth, strength);

            if (health / maxHealth <= strength && WhooshwhooshMod.RANDOM.nextBoolean()) {
                DamageSource damageSource = user.getDamageSources().mobAttack(user);
                livingTarget.damage(damageSource, health * (int) Math.sqrt(level));
//                WhooshwhooshMod.LOGGER.info(user.getName().getString() + " has been damaged!");
            }
        }
    }


    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof SwordItem || item instanceof AxeItem;
    }

    @Override
    public int getMinPower(int level) {
        return (int) Math.pow(2.6, level) + 1;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 36;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

}
