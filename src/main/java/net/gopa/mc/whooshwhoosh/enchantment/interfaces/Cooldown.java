package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public interface Cooldown extends Stored {

    String cdKey = "cd";

    int getDefaultCooldown(int level);

    default void refreshCD(Entity entity) {
        getDataSaver(entity)
                .putLong(cdKey, entity.getWorld().getTime())
                .save();
    }

    default long getLastFreshTime(Entity entity) {
        NbtCompound nbt = getData(entity);
        return nbt.getLong(cdKey);
    }

    default boolean isCooldownExpired(Entity entity, int level) {
        return entity.getWorld().getTime() - getLastFreshTime(entity) >= getDefaultCooldown(level);
    }
}
