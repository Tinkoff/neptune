package com.github.toy.constructor.core.api;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public interface Refreshable {
    void refresh();

    /**
     * Reads fields of some object. If it has some not empty field of type which extends {@link Refreshable}
     * then method invokes {@link #refresh()}.
     *
     * @param toBeRefreshed is an object which is supposed to have fields of type which extends {@link Refreshable}.
     */
    static void refresh(Object toBeRefreshed) {
        Class<?> clazz = toBeRefreshed.getClass();
        while (!clazz.equals(Object.class)) {
            Field[] fields = clazz.getDeclaredFields();

            stream(fields).forEach(field -> {
                int modifiers = field.getModifiers();

                if (!Modifier.isStatic(modifiers)) {
                    if (Refreshable.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        try {
                            Object value = field.get(toBeRefreshed);
                            ofNullable(value).ifPresent(o -> Refreshable.class.cast(o).refresh());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            clazz = clazz.getSuperclass();
        }
    }
}
