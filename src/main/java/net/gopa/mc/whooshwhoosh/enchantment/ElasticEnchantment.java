package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.toolkit.trigger.Trigger;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Triggerable;
import net.gopa.mc.whooshwhoosh.util.EquipmentSlotUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

@Trigger(TriggerPoint.ON_ENTITY_DAMAGE)
public class ElasticEnchantment extends Enchantment implements Triggerable {

    public ElasticEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.ARMOR, EquipmentSlotUtils.ARMOR_SLOTS);
    }

    @Override
    public ActionResult onTargetDamage(int level, LivingEntity source, Entity attacker, DamageSource damageSource) {
        if (canTrigger(level)) {
            double dx = attacker.getX() - source.getX();
            double dz = attacker.getZ() - source.getZ();

            double length = Math.sqrt(dx * dx + dz * dz);
            dx /= length;
            dz /= length;

            Vec3d newVelocity = getVec3d(attacker, dx, dz, level);
            attacker.setVelocity(newVelocity);
        }
        return ActionResult.PASS;
    }

    private static @NotNull Vec3d getVec3d(Entity attacker, double dx, double dz, int level) {
        float baseKnockback = 0.4f;
        float horizontalScale = 0.2f + level * 0.1f;

        double knockbackX = dx * baseKnockback * horizontalScale;
        double knockbackZ = dz * baseKnockback * horizontalScale;
        double knockbackY = 0.05f;

        Vec3d currentVelocity = attacker.getVelocity();
        return new Vec3d(
                currentVelocity.x + knockbackX,
                currentVelocity.y + knockbackY,
                currentVelocity.z + knockbackZ
        );
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof DyeableArmorItem;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
