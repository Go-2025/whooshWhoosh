package net.gopa.mc.whooshwhoosh.toolkit.trigger;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Cooldown;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 附魔触发器核心接口，定义所有可能的附魔触发事件点。<br>
 * 实现类应选择性地重写特定事件方法以添加自定义行为。
 *
 * <h4>设计原则：</h4>
 * <ul>
 *   <li><b>按需实现</b>：仅重写需要响应的事件方法</li>
 *   <li><b>概率控制</b>：通过{@link #getProbability(int)}控制基础触发率</li>
 *   <li><b>冷却集成</b>：配合{@link Cooldown}接口实现冷却机制</li>
 * </ul>
 *
 * <h4>事件处理流程：</h4>
 * <ol>
 *   <li>系统检测事件发生时调用对应方法
 *   <li>通过 {@link #canTrigger(int)} or {@link #canTrigger(Entity, int)} 检查基础条件
 *   <li>执行具体事件逻辑
 * </ol>
 *
 * 示例代码如下：
 * <blockquote><pre>{@code
 * @Trigger(TriggerPoint.ON_ATTACK)
 * class MyEnchantment extends Enchantment implements Triggerable {
 *     public MyEnchantment() {...}
 *
 *     @Override
 *     public ActionResult onAttack(int level, LivingEntity source, LivingEntity target) {
 *         if (canTrigger(level)) {
 *             // 具体逻辑
 *         } return ActionResult.PASS;
 *     }
 * }
 * }</pre></blockquote>
 *
 * @apiNote 所有 <b> 事件方法 </b> 默认抛出 {@link UnsupportedOperationException}
 * @see Cooldown 冷却接口
 * @since 1.0.0
 */
public interface Triggerable {

    /* ===== 事件方法 ===== */

    default ActionResult onAttack(int level, LivingEntity source, LivingEntity target) { throw new UnsupportedOperationException(); }
    default ActionResult onTargetDamage(int level, LivingEntity source, Entity attacker, DamageSource damageSource) { throw new UnsupportedOperationException(); }
    default ActionResult onCriticalHit(int level, Entity source, LivingEntity target) { throw new UnsupportedOperationException(); }
    default ActionResult onItemDamage(int level, LivingEntity source, ItemStack stack, int amount, Consumer<LivingEntity> breakCallback) { throw new UnsupportedOperationException(); }
    default ActionResult onItemBreak(int level, LivingEntity source, ItemStack stack, Consumer<LivingEntity> breakCallback) { throw new UnsupportedOperationException(); }
    default ActionResult onArrowHit(int level, LivingEntity target, ArrowEntity arrow) { throw new UnsupportedOperationException(); }
    default ActionResult onEntityJump(int level, LivingEntity source) { throw new UnsupportedOperationException(); }
    default ActionResult onItemEnch(int level, ItemStack source, NbtCompound nbt, Map<Enchantment, Integer> enchantments) { throw new UnsupportedOperationException(); }

    /* ===== 其他方法 ===== */

    default boolean canTrigger(int level) {
        return won(level);
    }

    default boolean canTrigger(Entity entity, int level) {
        return canTrigger(level)
                && this instanceof Cooldown cooldown
                && entity != null
                && cooldown.isCooldownExpired(entity, level)
                ;
    }
    default boolean won(int level) {
        return level > 0
                && (getProbability(level) == 1d || (getProbability(level) != 0d)
                && WhooshwhooshMod.RANDOM.nextDouble() < getProbability(level));
    }

    default int getOrder() {
        return 0;
    }

    default double getProbability(int level) {
        return 1d;  // (0, 1]
    }
}
