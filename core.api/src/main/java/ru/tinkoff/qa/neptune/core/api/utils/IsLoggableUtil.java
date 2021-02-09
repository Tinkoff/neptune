package ru.tinkoff.qa.neptune.core.api.utils;

import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

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
                if (!containsIgnoreCase(clazz.getName(), "cglib")) {
                    cls.getDeclaredMethod("toString");
                    return true;
                } else {
                    cls = cls.getSuperclass();
                }
            } catch (NoSuchMethodException e) {
                cls = cls.getSuperclass();
            }
        }

        return false;
    }

}
