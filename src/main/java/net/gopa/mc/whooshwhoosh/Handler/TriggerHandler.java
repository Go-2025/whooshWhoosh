package net.gopa.mc.whooshwhoosh.Handler;

import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.minecraft.util.ActionResult;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

public class TriggerHandler {

    private static final String packagePath = "net.gopa.mc.whooshwhoosh";
    private static final Set<String> paths = Set.of(
            "enchantment"
    );

    public static final Set<Class<? extends Triggerable>> TRIGGER_MAP = getTriggerMap();

    private TriggerHandler() {
    }

    private static Set<Class<? extends Triggerable>> getTriggerMap() {
        Set<Class<? extends Triggerable>> set = new HashSet<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            Enumeration<URL> resources = classLoader.getResources(packagePath.replace('.', '/'));
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                File dir = new File(url.getFile());
                scanDirectory(dir, packagePath, set, paths);
            }
        } catch (IOException ignored) {}
        return set;
    }

    private static void scanDirectory(File dir, String packageName, Set<Class<? extends Triggerable>> set) {
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory() && !file.getName().equals("mixin")) {
                scanDirectory(file, packageName + "." + file.getName(), set);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                putClassName(set, className);
            }
        }
    }

    private static void scanDirectory(File dir, String packageName, Set<Class<? extends Triggerable>> set, Set<String> paths) {
        for (String path : paths) {
            File subDir = new File(dir, path);
            if (subDir.exists() && subDir.isDirectory()) {
                scanDirectory(subDir, packageName + "." + path, set);
            }
        }
    }

    private static void putClassName(Set<Class<? extends Triggerable>> set, String className) {
        try {
            Class<?> cls = Class.forName(className);
            if (isTriggered(cls)) set.add(cls.asSubclass(Triggerable.class));
        } catch (ClassNotFoundException ignored) {

        }
    }

    private static boolean isTriggered(Class<?> cls) {
        return Triggerable.class.isAssignableFrom(cls) && cls.isAnnotationPresent(Trigger.class);
    }

    public static List<Class<? extends Triggerable>> getTriggerAtPoint(TriggerPoint point) {
        return TRIGGER_MAP.stream()
                .filter(cls -> Arrays.asList(cls.getAnnotation(Trigger.class).value()).contains(point))
                .toList();
    }
}
