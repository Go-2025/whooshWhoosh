package net.gopa.mc.whooshwhoosh.interfaces;

import net.minecraft.nbt.NbtCompound;

public interface Persistent {
    NbtCompound getData();
    void setData(NbtCompound nbt);
}
