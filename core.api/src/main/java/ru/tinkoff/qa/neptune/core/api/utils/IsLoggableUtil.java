package ru.tinkoff.qa.neptune.core.api.utils;

import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;

public final class IsLoggableUtil {

    private IsLoggableUtil() {
        super();
    }

    public static boolean isLoggable(Object toBeDescribed) {
        if (toBeDescribed == null) {
            return true;
        }

        var clazz = toBeDescribed.getClass();
        if (clazz.isPrimitive() || isPrimitiveOrWrapper(clazz) || String.class.isAssignableFrom(clazz)
                || clazz.isEnum()) {
            return true;
        }

        var cls = clazz;
        while (!cls.equals(Object.class)) {
            try {
                cls.getDeclaredMethod("toString");
                return true;
            } catch (NoSuchMethodException e) {
                cls = cls.getSuperclass();
            }
        }

        return false;
    }

}
