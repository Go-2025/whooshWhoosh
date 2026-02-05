package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.minecraft.nbt.NbtCompound;

public interface Persistent {
    NbtCompound getData();
    void setData(NbtCompound nbt);
}
