package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.gopa.mc.whooshwhoosh.toolkit.trigger.Trigger;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Triggerable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;

import java.util.Map;

/**
 * 标记可消耗的附魔类型。实现此接口的附魔在被附魔时会被消耗，
 * 并执行自定义效果逻辑。<br>
 *
 * <h4>实现要求：</h4>
 * <ol>
 *   <li>必须实现 {@link #consume} 定义消耗效果</li>
 *   <li>必须使用注解 {@link Trigger @Trigger(TriggerPoint.ON_ITEM_ENCH)} 标记实现类</li>
 * </ol>
 * @since 1.0.0
 */
public interface Consumable extends Triggerable {

    ActionResult consume(int level, ItemStack source, Map<Enchantment, Integer> enchantments);

    @Override
    default ActionResult onItemEnch(int level, ItemStack source, NbtCompound nbt, Map<Enchantment, Integer> enchantments) {
        ActionResult result = consume(level, source, enchantments);
        remove(source, nbt);
        return result;
    }

    default void remove(ItemStack stack, NbtCompound nbt) {
        stack.getEnchantments().remove(nbt);
    }
}
