package ru.tinkoff.qa.neptune.core.api;

import java.util.function.*;

import static java.lang.Integer.toHexString;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;
import static org.apache.commons.lang3.StringUtils.isBlank;

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

        if (Function.class.isAssignableFrom(clazz) || BiFunction.class.isAssignableFrom(clazz) ||
                Consumer.class.isAssignableFrom(clazz) || BiConsumer.class.isAssignableFrom(clazz)
                || Predicate.class.isAssignableFrom(clazz)) {
            return hasReadableDescription(toBeDescribed);
        }

        if (LoggableObject.class.isAssignableFrom(clazz)) {
            return hasReadableDescription(toBeDescribed);
        }

        return false;
    }

    private static boolean hasReadableDescription(Object toBeDescribed) {
        var stringDescription = valueOf(toBeDescribed);
        return !isBlank(stringDescription) && !valueOf(toBeDescribed).equals(format("%s@%s", toBeDescribed.getClass().getName(),
                toHexString(toBeDescribed.hashCode())));
    }
}
