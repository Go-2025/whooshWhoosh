package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.toolkit.data.DataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public interface Stored {

    String ROOT_KEY = WhooshwhooshMod.MOD_ID + "_data";

    default String getClassKey() {
        return this.getClass().getSimpleName();
    }

    default DataSaver getDataSaver(Entity entity) {
        DataSaver saver = DataSaver.of(entity);
        if (!saver.getData().contains(getClassKey()))
            saver.put(getClassKey(), new NbtCompound()).save();
        return saver.jumpTo(getClassKey());
    }

    default NbtCompound getData(Entity entity) {
        return getDataSaver(entity).getData();
    }
}
