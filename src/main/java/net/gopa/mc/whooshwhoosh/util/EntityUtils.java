package net.gopa.mc.whooshwhoosh.util;

import net.gopa.mc.whooshwhoosh.toolkit.data.DataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public final class EntityUtils {

    private EntityUtils() {
    }

    public static float getLastBowPullProgress(Entity entity) {
        return DataSaver.of(entity)
                .getData()
                .getFloat("last_bow_pull_progress");
    }
}
