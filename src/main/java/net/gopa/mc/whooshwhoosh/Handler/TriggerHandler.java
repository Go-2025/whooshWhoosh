package net.gopa.mc.whooshwhoosh.Handler;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.mixin.accessors.EnchantmentAccessorMixin;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.gopa.mc.whooshwhoosh.toolkit.scan.ClassScanner;
import net.gopa.mc.whooshwhoosh.util.EnchantmentUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.ActionResult;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TriggerHandler {

    private static final String packagePath = "net.gopa.mc.whooshwhoosh";
    private static final Set<String> paths = Set.of(
            "enchantment"
    );
    public static final List<Class<?>> TRIGGERS = getTriggerMap();
    public static final Map<TriggerPoint, Class<?>[]> POINT_MAP = createTriggerPointMap();

    private static List<Class<?>> getTriggerMap() {
        List<Class<?>> lst = new ArrayList<>();
        try {
            for (String path : paths) {
                List<Class<?>> classes = ClassScanner.findClassesInPackage(packagePath + "." + path, TriggerHandler::isTriggered);
                lst.addAll(classes);
            }
        } catch (IOException | ClassNotFoundException e) {
            WhooshwhooshMod.LOGGER.warn("Error loading triggers", e);
        }
        return lst;
    }

    private static Map<TriggerPoint, Class<?>[]> createTriggerPointMap() {
        Map<TriggerPoint, Class<?>[]> map = new HashMap<>();

        List<TriggerPoint> values = new ArrayList<>(List.of(TriggerPoint.values()));
        values.remove(TriggerPoint.OTHER);

        for (TriggerPoint point : values) {
            List<Class<?>> classes = new ArrayList<>();
            for (Class<?> cls : TriggerHandler.TRIGGERS) {
                if (point.hasTriggerPoint(cls)) {
                    classes.add(cls);
                }
            }
            map.put(point, classes.toArray(Class<?>[]::new));
        }
        return map;
    }

    private static boolean isTriggered(Class<?> cls) {
        return Triggerable.class.isAssignableFrom(cls) && cls.isAnnotationPresent(Trigger.class);
    }
}
