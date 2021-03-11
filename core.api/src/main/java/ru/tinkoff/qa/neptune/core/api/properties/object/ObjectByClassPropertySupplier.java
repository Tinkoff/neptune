package ru.tinkoff.qa.neptune.core.api.properties.object;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import static java.lang.Class.forName;

/**
 * This interface is designed to read properties and return single instances.
 * This supplier is supposed to create some object by invocation of default constructor.
 * Resulted object is used as value of the property. The correct format of the property value is
 * fully qualified name of the class.
 *
 * @param <T> is a type of a value.
 */
@SuppressWarnings("unchecked")
public interface ObjectByClassPropertySupplier<T> extends PropertySupplier<T, Class<? extends T>> {

    @Override
    default T parse(String s) {
        try {
            var clazz = ((Class<T>) forName(s));
            var c = clazz.getConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default String readValuesToSet(Class<? extends T> value) {
        return value.getName();
    }
}
