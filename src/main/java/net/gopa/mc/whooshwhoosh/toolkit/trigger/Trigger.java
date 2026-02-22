package net.gopa.mc.whooshwhoosh.toolkit.trigger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记附魔触发点的注解，用于将实现类绑定到特定的游戏事件。<br>
 * 配合{@link Triggerable Triggerable}接口使用，实现事件驱动的效果逻辑。
 *
 * <h4>核心功能：</h4>
 * <ul>
 *   <li>声明类监听的触发点（如攻击、暴击等）</li>
 *   <li>控制多触发点处理顺序（通过{@code order}属性）</li>
 *   <li>运行时动态注册事件处理器</li>
 * </ul>
 *
 * <h4>使用规则：</h4>
 * <ol>
 *   <li>仅能用于类级别（TYPE元素类型）</li>
 *   <li>必须配合{@link Triggerable Triggerable}接口实现</li>
 *   <li>多个触发点用数组形式声明（如{@code @Trigger({A,B})}）</li>
 * </ol>
 *
 * @see Triggerable Triggerable 事件处理核心接口
 * @see TriggerPoint 触发点枚举定义
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Trigger {
    TriggerPoint[] value() default {};
}
