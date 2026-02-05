package net.gopa.mc.whooshwhoosh.enchantment.interfaces;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;

public interface Triggerable {

    Random RANDOM = WhooshwhooshMod.RANDOM;

    @PreCheck
    default void onAttack(int level, LivingEntity target, Entity attacker, DamageSource damageSource) {}
    @PreCheck
    default void onTargetDamage(int level, LivingEntity target, Entity attacker, DamageSource damageSource) {}
    @PreCheck
    default void onCriticalHit(int level, LivingEntity target, Entity attacker) {}
    @PreCheck
    default void onItemDamage(int level, ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {}

    default boolean checkOnAttack(int level, LivingEntity target, Entity attacker, DamageSource damageSource) {return true;}
    default boolean checkOnTargetDamage(int level, LivingEntity target, Entity attacker, DamageSource damageSource) {return true;}
    default boolean checkOnCriticalHit(int level, LivingEntity target, Entity attacker) {return true;}
    default boolean checkOnItemDamage(int level, ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {return true;}

    default boolean checkAll(int level) {
        return (
                getProbability(level) == null || RANDOM.nextDouble() < getProbability(level)
        );
    }

    default Double getProbability(int level) {
        return null;  // (0, 1]
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface PreCheck {
//        boolean override();
    }

    record CheckHandler(Object target) implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (target instanceof Triggerable triggerableTarget) {
                if (!triggerableTarget.checkAll((int) args[0]))
                    return null;
            }
            PreCheck annotation = method.getAnnotation(PreCheck.class);
            if (annotation != null) {
                String checkMethodName = "check"
                        + Character.toUpperCase(method.getName().charAt(0))
                        + method.getName().substring(1);
                Class<?> clazz = target.getClass();
                try {
                    Method checkMethod = clazz.getMethod(checkMethodName, method.getParameterTypes());
                    boolean canTrigger = (boolean) checkMethod.invoke(target, args);
                    if (!canTrigger) return null;
                } catch (NoSuchMethodException ignored) {}
            }
            return method.invoke(target, args);
        }
    }

    class CheckProxyFactory {
        @SuppressWarnings("unchecked")
        public static <T> T createProxy(T target, Class<T> interfaceClass) {
            return (T) Proxy.newProxyInstance(
                    interfaceClass.getClassLoader(),
                    new Class<?>[]{interfaceClass},
                    new CheckHandler(target)
            );
        }
    }
}
