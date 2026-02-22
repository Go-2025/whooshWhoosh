package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.toolkit.dataTool.DataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

/**
 * 实体数据存储接口，提供标准化的NBT数据存取方案。<br>
 * 实现类可将任意数据关联到游戏实体，数据自动存储在统一的命名空间下。
 *
 * <h4>数据存储结构：</h4>
 * <pre>
 *   Entity.NBT
 *   └─ ROOT_KEY ("{modid}_data")       // 模组专属根标签
 *      ├─ ClassAKey                    // 实现类A的数据容器
 *      │   ├─ key1: value1
 *      │   └─ key2: value2
 *      └─ ClassBKey                    // 实现类B的数据容器
 *          └─ ...
 * </pre>
 *
 * <h4>使用场景：</h4>
 * <ul>
 *   <li>持久化附魔状态（如充能进度）</li>
 *   <li>存储实体专属配置（如技能冷却）</li>
 *   <li>跨游戏会话保存临时数据</li>
 * </ul>
 *
 * @apiNote 数据存储在实体的持久化NBT中，随实体保存/加载
 * @see DataSaver
 * @since 1.0.0
 */
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
