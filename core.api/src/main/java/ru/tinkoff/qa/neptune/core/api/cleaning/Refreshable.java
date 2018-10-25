package ru.tinkoff.qa.neptune.core.api.cleaning;

import java.lang.reflect.Modifier;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public interface Refreshable {
    /**
     * Reads fields of some object. If it has some not empty field of type which extends {@link Refreshable}
     * then method invokes {@link Refreshable#refresh()}.
     *
     * @param toBeRefreshed is an object which is supposed to have fields of type which extends {@link Refreshable}.
     */
    static void refresh(Object toBeRefreshed) {
        var clazz = toBeRefreshed.getClass();
        while (!clazz.equals(Object.class)) {
            var fields = clazz.getDeclaredFields();

            stream(fields).forEach(field -> {
                var modifiers = field.getModifiers();

                if (!Modifier.isStatic(modifiers)) {
                    if (Refreshable.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        try {
                            var value = field.get(toBeRefreshed);
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

    void refresh();
}
