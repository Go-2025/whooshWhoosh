package net.gopa.mc.whooshwhoosh.toolkit.dataTool;

import net.gopa.mc.whooshwhoosh.interfaces.Persistent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.List;
import java.util.ListIterator;

public class DataSaver {

    private final Persistent entity;
    private NbtCompound data;
    private String currPath = "";

    private DataSaver(Persistent entity) {
        this.entity = entity;
        this.data = entity.getData();
    }

    public NbtCompound getData() {
        return data;
    }

    public static DataSaver of(Entity entity) {
        if (entity instanceof Persistent persistent) {
            return new DataSaver(persistent);
        }
        throw new IllegalArgumentException("Entity must implement Persistent");
    }

    public void save() {
        ListIterator<String> keys = List.of(currPath.split("\\.")).listIterator();
        if (keys.hasPrevious()) {
            NbtCompound data = save(keys, entity.getData());
            entity.setData(data);
        }
//        WhooshwhooshMod.LOGGER.info("DataSaver: {}, {}", entity.getData(), currPath);
    }

    private NbtCompound save(ListIterator<String> keys, NbtCompound curr) {
        if (!keys.hasNext()) {
            curr.put(keys.next(), data);
            return curr;
        }
        String key = keys.next();
        NbtCompound next = curr.getCompound(key);
        NbtCompound last = save(keys, next);
        curr.put(key, last);
        return curr;
    }

    public DataSaver jumpTo(String key) {
        if (data.contains(key, NbtElement.COMPOUND_TYPE)) {
            save();

            data = data.getCompound(key);
            applyPath(key);
        }
        return this;
    }

    private void applyPath(String subpath) {
        if (currPath.isEmpty()) {
            currPath = subpath;
        } else {
            currPath += "." + subpath;
        }
    }

    public DataSaver returnRoot() {
        save();

        data = entity.getData();
        return this;
    }

    public DataSaver remove(String key) {
        data.remove(key);
        return this;
    }

    public DataSaver putShort(String key, short value) {
        data.putShort(key, value);
        return this;
    }

    public DataSaver putByte(String key, byte value) {
        data.putByte(key, value);
        return this;
    }

    public DataSaver putBoolean(String key, boolean value) {
        data.putBoolean(key, value);
        return this;
    }

    public DataSaver putInt(String key, int value) {
        data.putInt(key, value);
        return this;
    }

    public DataSaver putLong(String key, long value) {
        data.putLong(key, value);
        return this;
    }

    public DataSaver putString(String key, String value) {
        data.putString(key, value);
        return this;
    }

    public DataSaver putFloat(String key, float value) {
        data.putFloat(key, value);
        return this;
    }

    public DataSaver putDouble(String key, double value) {
        data.putDouble(key, value);
        return this;
    }

    public DataSaver putByteArray(String key, byte[] value) {
        data.putByteArray(key, value);
        return this;
    }

    public DataSaver putIntArray(String key, int[] value) {
        data.putIntArray(key, value);
        return this;
    }

    public DataSaver putLongArray(String key, long[] value) {
        data.putLongArray(key, value);
        return this;
    }

    public DataSaver put(String key, NbtCompound value) {
        data.put(key, value);
        return this;
    }

    public DataSaver putUuid(String key, java.util.UUID value) {
        data.putUuid(key, value);
        return this;
    }

    @Override
    public String toString() {
        return entity.toString() + ": " + data.toString();
    }
}
