package ru.tinkoff.qa.neptune.core.api.properties.object;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import static java.lang.Class.forName;
import static java.lang.String.format;

/**
 * This interface is designed to read properties and return single instances of {@link Supplier}.
 * This supplier is supposed to create some object and return it via {@link Supplier#get()}.
 * Resulted object is used as value of the property. The correct format of the property value is
 * fully qualified name of the {@link Supplier} implementor. It should be a subclass of
 * abstraction declared as generic parameter.
 *
 * <p>NOTE!</p>
 * <p></p>
 * Implementor of {@link Supplier} should have default constructor or it may have not any declared constructor.
 *
 * @param <T> is a type of a {@link Supplier#get()} implementor.
 */
@SuppressWarnings("unchecked")
public interface ObjectPropertySupplier<T extends Supplier<?>> extends PropertySupplier<T> {

    @Override
    default T parse(String s) {
        try {
            var clazz = ((Class<T>) forName(s));
            var c = clazz.getConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(format("Class %s is not found", s), e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(format("It is impossible to use an instance of the class %s as a value of the property %s",
                    s,
                    getName()), e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(format("Class %s has no default constructor", s), e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException(format("It is impossible to create an instance of %s for some reason", s),
                    e);
        }
    }
}
