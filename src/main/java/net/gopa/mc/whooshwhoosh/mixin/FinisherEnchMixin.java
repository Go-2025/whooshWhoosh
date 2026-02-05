package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({LivingEntity.class, PlayerEntity.class})
public class FinisherEnchMixin {

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float takeDamage(float amount, DamageSource source, float originalAmount) {
        LivingEntity target = (LivingEntity) (Object) this;
        Entity attacker = source.getAttacker();

        if (!(attacker instanceof LivingEntity livingAttacker)) return amount;

        ItemStack stack = livingAttacker.getMainHandStack();
        int level = EnchantmentHelper.getLevel(EnchantmentsRegistry.FINISHER.get(), stack);
        if (level <= 0) return amount;

        float strength =  (level - 0.5f) / 20f;
        float health = target.getHealth();
        float maxHealth = target.getMaxHealth();

        if (health / maxHealth <= strength) {
            amount = Math.max(amount * (int) (Math.sqrt(level + 1) * 1.4f), amount);
        }
        return amount;
    }
}