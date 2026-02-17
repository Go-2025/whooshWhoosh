package net.gopa.mc.whooshwhoosh.enchantment.annotation;

import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Trigger {
    TriggerPoint[] value() default {};
}
