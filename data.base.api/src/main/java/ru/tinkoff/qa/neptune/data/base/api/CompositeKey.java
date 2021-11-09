package ru.tinkoff.qa.neptune.data.base.api;

import java.io.Serializable;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.*;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

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
@Deprecated(forRemoval = true)
public abstract class CompositeKey extends OrmObject implements Serializable {

    @Override
    public int hashCode() {
        var result = 0;
        Class<?> clazz = this.getClass();
        while (!clazz.equals(Object.class)) {
            var fields = stream(clazz.getDeclaredFields()).filter(field -> !isStatic(field.getModifiers()))
                    .collect(toList());
            for (var f : fields) {
                f.setAccessible(true);
                try {
                    var v = f.get(this);
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

    @Override
    public String toString() {
        var thisClass = this.getClass();
        var key = new StringBuffer();
        var thisKey = this;
        stream(thisClass.getDeclaredFields())
                .filter(field -> {
                    var modifiers = field.getModifiers();
                    return !isStatic(modifiers) && !isFinal(modifiers) && isPublic(modifiers);
                }).forEach(field -> {
            try {
                key.append(format("%s = %s, ", field.getName(), field.get(thisKey)));
            } catch (Exception ignored) {
            }
        });

        return key.toString().trim();
    }

    @Override
    public boolean equals(Object obj) {
        var toCheck = this;
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
}
