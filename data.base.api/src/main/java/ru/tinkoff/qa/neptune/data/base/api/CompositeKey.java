package ru.tinkoff.qa.neptune.data.base.api;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

/**
 * This class is designed to implement classes of composite key objects.
 * It is supposed to be used at the case below
 * {@code @PersistenceCapable(table = "TABLE_NAME", objectIdClass = CompositeKeySubclass.class)}
 *
 * WARNING: it is necessary to override following methods:
 * <p>{@link #hashCode()}</p>
 * <p>{@link #equals(Object)}</p>
 * <p>{@link #toString()}</p>
 *
 * It is enough
 * <p>
 *     {@code 'public int hashCode() {
 *             return super.hashCode();
 *            }'}
 * </p>
 */
public abstract class CompositeKey implements Serializable {

    @Override
    public int hashCode() {
        int result = 0;
        Class<?> clazz = this.getClass();
        while (!clazz.equals(Object.class)) {
            List<Field> fields = stream(clazz.getDeclaredFields()).filter(field -> !isStatic(field.getModifiers()))
                    .collect(Collectors.toList());
            for (Field f : fields) {
                f.setAccessible(true);
                try {
                    Object v = f.get(this);
                    if (v == null) {
                        continue;
                    }
                    result = result ^ v.hashCode();
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            clazz = clazz.getSuperclass();
        }

        return result;
    }

    private boolean equalsByFields(Object obj) {
        Class<?> clazz = this.getClass();
        while (!clazz.equals(Object.class)) {
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
        CompositeKey toCheck = this;
        return ofNullable(obj)
                .map(o -> {
                    if (toCheck == o) {
                        return true;
                    }
                    return toCheck.getClass().equals(obj.getClass()) &&
                            toCheck.equalsByFields(obj);
                })
                .orElse(false);
    }

    @Override
    public String toString() {
        return format("%s:%s", this.getClass(), hashCode());
    }
}
