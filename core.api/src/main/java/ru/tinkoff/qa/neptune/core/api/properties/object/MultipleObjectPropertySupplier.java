package ru.tinkoff.qa.neptune.core.api.properties.object;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;

import static java.lang.Class.forName;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * This interface is designed to read properties and return lists of {@link Supplier} instances.
 * This supplier is supposed to create some object and return it via {@link Supplier#get()}.
 * Resulted list of objects is used as value of the property. The correct format of the property value is
 * comma-separated string that consists of fully qualified class names. Each class should be a subclass of
 * abstraction declared as generic parameter.
 *
 * <p>NOTE!</p>
 * <p></p>
 * Implementor of {@link Supplier} should have default constructor or it may have not any declared constructor.
 *
 * @param <T> is a type of a {@link Supplier#get()} implementor.
 */
@SuppressWarnings("unchecked")
public interface MultipleObjectPropertySupplier<T extends Supplier<?>> extends PropertySupplier<List<T>> {

    default List<T> get() {
        return returnOptionalFromEnvironment().map(s -> stream(s.split(",")).map(s1 -> {
            try {
                var clazz = ((Class<T>) forName(s));
                var c = clazz.getConstructor();
                c.setAccessible(true);
                return c.newInstance();
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(format("Class %s is not found", s), e);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(format("It is impossible to use an instance of the class %s as one of values of the property %s",
                        s,
                        getPropertyName()), e);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(format("Class %s has no default constructor", s), e);
            } catch (IllegalAccessException| InstantiationException| InvocationTargetException e) {
                throw new IllegalArgumentException(format("It is impossible to create an instance of %s for some reason", s),
                        e);
            }
        }).collect(toList())).orElse(null);
    }
}
