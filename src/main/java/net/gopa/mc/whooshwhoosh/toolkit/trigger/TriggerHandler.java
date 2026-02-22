package net.gopa.mc.whooshwhoosh.toolkit.trigger;

import net.gopa.mc.whooshwhoosh.Handler.Handler;
import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.toolkit.scan.ClassScanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class TriggerHandler extends Handler {

    private static final String packagePath = "net.gopa.mc.whooshwhoosh";
    private static final Set<String> paths = Set.of(
            "enchantment"
    );
    public static final List<Class<?>> TRIGGERS = createTriggerMap();
    public static final Map<TriggerPoint, Class<?>[]> POINT_MAP = createTriggerPointMap();
    protected final TriggerPoint point;

    public TriggerHandler(TriggerPoint point) {
        this.point = point;
    }

    private static List<Class<?>> createTriggerMap() {
        List<Class<?>> lst = new ArrayList<>();
        try {
            for (String path : paths) {
                List<Class<?>> classes = ClassScanner.findClassesInPackage(packagePath + "." + path, TriggerHandler::isTriggered);
                classes.sort(Comparator.comparingInt(TriggerHandler::getOrder));
                lst.addAll(classes);
            }
        } catch (IOException | ClassNotFoundException e) {
            WhooshwhooshMod.LOGGER.warn("Error loading triggers", e);
        }
        return lst;
    }

    private static int getOrder(Class<?> cls) {
        final String methodName = "getOrder";
        try {
            Object instance = cls.getDeclaredConstructor().newInstance();
            for (Method method : cls.getMethods()) {
                boolean isTargetMethod = method.getName().equals(methodName)
                        && method.getParameterCount() == 0
                        && method.getReturnType().equals(int.class);
                if (isTargetMethod) return (int) method.invoke(instance);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            WhooshwhooshMod.LOGGER.error("Error loading triggers", e);
        }
        return 0;
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

    protected boolean isOtherPoint() {
        return point == TriggerPoint.OTHER;
    }
}
