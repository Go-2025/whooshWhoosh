package net.gopa.mc.whooshwhoosh.util;

import java.lang.annotation.Annotation;

public final class AnnotationUtil {

    public static <T extends Annotation> T getClassAnnotation(Class<?> clazz, Class<T> annotationClass) {
        if (clazz.isAnnotationPresent(annotationClass)) {
            return clazz.getAnnotation(annotationClass);
        }
        return null;
    }
}
