package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.gopa.mc.whooshwhoosh.toolkit.trigger.Triggerable;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

/**
 * 冷却时间管理接口，实现基于游戏刻的冷却机制。<br>
 * 继承{@link Stored}接口，使用NBT持久化存储冷却时间戳。
 *
 * <h4>核心机制：</h4>
 * <ul>
 *   <li>使用{@link net.minecraft.world.World#getTime() World.grtTime()}获取游戏刻时间戳</li>
 *   <li>冷却判断公式：当前时间 - 上次刷新时间 ≥ 冷却时长</li>
 *   <li>数据存储于实体的{@link Stored#ROOT_KEY}命名空间下</li>
 * </ul>
 *
 * <h4>数据存储结构：</h4>
 * <pre>
 *   Entity.NBT
 *   └─ {@literal @}RootKey  // Stored.ROOT_KEY ("{modid}_data")
 *      └─ ClassKey         // getClassKey()
 *         ├─ cd: 123456789L // 冷却时间戳（游戏刻）
 *         └─ ...           // 其他数据
 * </pre>
 *
 * <h4>使用示例：</h4>
 * <pre>{@code
 *   public class MyEnchantment extends Enchantment implements Cooldown {
 *
 *       public MyEnchantment() {...}
 *
 *       @Override
 *       public ActionResult onAttack(int level, LivingEntity source, LivingEntity target) {
 *           if (canTrigger(source, level)) {
 *               // 具体逻辑
 *           refreshCD(user); // 重置冷却
 *           } return ActionResult.PASS;
 *       }
 *
 *       @Override
 *       public int getDefaultCooldown(int level) {
 *           return 20 * level; // 每级1秒冷却（20刻）
 *       }
 *   }
 * }</pre>
 *
 * @apiNote 时间戳精度为游戏刻（1刻=1/20秒）
 * @see Stored
 * @see net.minecraft.world.World#getTime
 * @see Triggerable#canTrigger(Entity, int)
 * @since 1.0.0
 */
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
