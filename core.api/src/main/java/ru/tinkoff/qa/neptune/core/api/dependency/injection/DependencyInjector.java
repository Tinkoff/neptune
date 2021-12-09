package ru.tinkoff.qa.neptune.core.api.dependency.injection;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.addAll;

/**
 * It is designed for the filling of not static and not final fields of objects.
 * It is recommended to implement by classes which have no declared constructor/constructor without parameters.
 */
public interface DependencyInjector {

    List<DependencyInjector> dependencyInjectors = getDependencyInjectors();

    private static List<DependencyInjector> getDependencyInjectors() {
        var iterator = load(DependencyInjector.class).iterator();
        Iterable<DependencyInjector> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false).collect(toList());
    }

    /**
     * Fills fields of an object with values
     *
     * @param o whose fields should be filled with values
     */
    static void injectValues(Object o) {
        checkNotNull(o);
        var clz = o.getClass();
        Field[] fields1 = new Field[]{};

        while (!clz.equals(Object.class)) {
            fields1 = addAll(fields1, clz.getDeclaredFields());
            clz = clz.getSuperclass();
        }

        if (fields1.length == 0) {
            return;
        }

        var fields2 = fields1;

        dependencyInjectors.forEach(injector -> {
            stream(fields2).forEach(f -> {
                var m = f.getModifiers();
                f.setAccessible(true);
                var type = f.getType();

                try {
                    var val = f.get(o);
                    if (!isStatic(m) && !isFinal(m) && injector.toSet(f) && (type.isPrimitive() || val == null)) {
                        f.set(o, injector.getValueToSet(f));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    /**
     * Is it possible/correct/necessary to set value to a field
     *
     * @param field to set a value to
     * @return to set value here or not
     */
    boolean toSet(Field field);

    /**
     * Gets/creates an object which is used as a field value
     *
     * @param field which has to be filled
     * @return a new value of a field
     */
    Object getValueToSet(Field field);
}
