package net.gopa.mc.whooshwhoosh.toolkit.util;

import net.gopa.mc.whooshwhoosh.toolkit.dataTool.DataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.stream.Stream;

public final class EntityUtils {

    private EntityUtils() {
    }

    public static boolean isServer(Entity entity) {
        return !entity.getWorld().isClient;
    }

    public static float getLastBowPullProgress(Entity entity) {
        return DataSaver.of(entity)
                .getData()
                .getFloat("last_bow_pull_progress");
    }

    public static List<ItemStack> getEquippedItems(Entity entity, EquipmentSlot... slots) {
        if (entity instanceof LivingEntity livingEntity) {
            return Stream.of(slots)
                    .map(livingEntity::getEquippedStack)
                    .toList();
        }
        return List.of();
    }

    public static int computeFallDamage(LivingEntity self, float fallDistance, float damageMultiplier) {
//        if (self.getType().isIn(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
//            return 0;
//        }
        StatusEffectInstance statusEffectInstance = self.getStatusEffect(StatusEffects.JUMP_BOOST);
        float f = statusEffectInstance == null ? 0.0f : (float)(statusEffectInstance.getAmplifier() + 1);
        return MathHelper.ceil((fallDistance - 3.0f - f) * damageMultiplier);
    }
}
