package ru.tinkoff.qa.neptune.data.base.api;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

/**
 * This abstract class is designed to mark persistable classes.
 */
public abstract class PersistableObject {

    private boolean equalsByFields(Object obj) {
        Class<?> clazz = this.getClass();
        while (!clazz.equals(PersistableObject.class)) {
            List<Field> fields = stream(clazz.getDeclaredFields()).filter(field -> !isStatic(field.getModifiers()))
                    .collect(Collectors.toList());
            for (Field f: fields) {
                f.setAccessible(true);
                try {
                    Object v1 = f.get(this);
                    Object v2 = f.get(obj);

                    if (v1 == null && v2 == null) {
                        continue;
                    }

                    if ((v1 != null && v2 == null) || (v1 == null && v2 != null)) {
                        return false;
                    }

                    if (v1.equals(v2)) {
                        continue;
                    }

                    return false;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            clazz = clazz.getSuperclass();
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        PersistableObject toCheck = this;
        return ofNullable(obj)
                .map(o -> {
                    if (toCheck == o) {
                        return true;
                    }
                    else if (!PersistableObject.class.isAssignableFrom(obj.getClass())) {
                        return false;
                    }
                    return toCheck.getClass().equals(obj.getClass()) &&
                            toCheck.equalsByFields(obj);
                })
                .orElse(false);
    }
}
