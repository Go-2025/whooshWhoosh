package net.gopa.mc.whooshwhoosh.toolkit.scan;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {

    public static List<Class<?>> findClassesInPackage(String packageName, Function<Class<?>, Boolean> fun)
            throws IOException, ClassNotFoundException {
        List<Class<?>> classNames = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');

        Enumeration<URL> resources = Thread.currentThread()
                .getContextClassLoader()
                .getResources(packagePath);

        while (resources.hasMoreElements()) {
            URL res = resources.nextElement();
            String protocol = res.getProtocol();

            if ("file".equals(protocol)) {
                // 处理文件夹
                File dir = new File(URLDecoder.decode(res.getFile(), StandardCharsets.UTF_8));
                scanDirectory(dir, packageName, classNames, fun);
            } else if ("jar".equals(protocol)) {
                // 处理JAR包
                JarFile jarFile = ((JarURLConnection) res.openConnection()).getJarFile();
                scanJarFile(jarFile, packagePath, classNames, fun);
            }
        }
        return classNames;
    }

    public static List<Class<?>> findClassesInPackage(String packageName) throws IOException, ClassNotFoundException {
        return findClassesInPackage(packageName, clazz -> true);
    }

    private static void scanDirectory(File dir, String packageName, List<Class<?>> classNames, Function<Class<?>, Boolean> fun) throws ClassNotFoundException {
        if (!dir.exists()) return;

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName(), classNames, fun);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = Class.forName(className);

                if (fun.apply(clazz)) classNames.add(clazz);
            }
        }
    }

    private static void scanJarFile(JarFile jarFile, String packagePath, List<Class<?>> classNames, Function<Class<?>, Boolean> fun) throws ClassNotFoundException {
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();

            if (entryName.startsWith(packagePath) && entryName.endsWith(".class")) {
                String className = entryName
                        .replace('/', '.')
                        .substring(0, entryName.length() - 6);

                if (className.startsWith(packagePath.replace('/', '.'))) {
                    Class<?> clazz = Class.forName(className);
                    if (fun.apply(clazz)) classNames.add(clazz);
                }
            }
        }
    }
}
