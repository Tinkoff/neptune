package com.github.toy.constructor.core.api.cleaning;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public class RefreshAndStopUtil {
    /**
     * Reads fields of some object. If it has some not empty field of type which extends {@link Refreshable}
     * then method invokes {@link Refreshable#refresh()}.
     *
     * @param toBeRefreshed is an object which is supposed to have fields of type which extends {@link Refreshable}.
     */
    public static void refresh(Object toBeRefreshed) {
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
                            ofNullable(value).ifPresent(o -> ((Refreshable) o).refresh());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Reads fields of some object. If it has some not empty field of type which extends {@link Stoppable}
     * then method invokes {@link Stoppable#shutDown()}.
     *
     * @param toBeStopped is an object which is supposed to have fields of type which extends {@link Stoppable}.
     */
    public static void shutDown(Object toBeStopped) {
        Class<?> clazz = toBeStopped.getClass();
        while (!clazz.equals(Object.class)) {
            Field[] fields = clazz.getDeclaredFields();

            stream(fields).forEach(field -> {
                int modifiers = field.getModifiers();

                if (!Modifier.isStatic(modifiers)) {
                    if (Stoppable.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        try {
                            Object value = field.get(toBeStopped);
                            ofNullable(value).ifPresent(o -> Stoppable.class.cast(o).shutDown());
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
