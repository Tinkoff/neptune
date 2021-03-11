package ru.tinkoff.qa.neptune.core.api.properties.object;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import static java.lang.Class.forName;
import static java.lang.String.format;

/**
 * This interface is designed to read properties and return single instances
 * by invocation of {@link Supplier#get()}. Resulted object is used as value
 * of the property. The correct format of the property value is fully qualified
 * name of the class of {@link Supplier}.
 *
 * <p>NOTE!</p>
 * <p></p>
 * Implementor of {@link Supplier} should have default constructor or it may have not any declared constructor.
 *
 * @param <R> is a type of the property value.
 * @param <T> is a type of a {@link Supplier} implementor.
 */
@SuppressWarnings("unchecked")
public interface ObjectPropertySupplier<R, T extends Supplier<R>> extends PropertySupplier<R, Class<? extends T>> {

    @Override
    default R parse(String s) {
        try {
            var clazz = ((Class<T>) forName(s));
            var c = clazz.getConstructor();
            c.setAccessible(true);
            return c.newInstance().get();
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(format("Class %s is not found", s), e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(format("Class %s has no default constructor", s), e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException(format("It is impossible to create an instance of %s for some reason", s), e);
        }
    }

    @Override
    default String readValuesToSet(Class<? extends T> value) {
        return value.getName();
    }
}
