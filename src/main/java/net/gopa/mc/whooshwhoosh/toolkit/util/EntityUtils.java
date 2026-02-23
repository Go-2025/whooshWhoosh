package net.gopa.mc.whooshwhoosh.toolkit.util;

import net.gopa.mc.whooshwhoosh.toolkit.dataTool.DataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Stream;

public final class EntityUtils {

    private EntityUtils() {
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
}
