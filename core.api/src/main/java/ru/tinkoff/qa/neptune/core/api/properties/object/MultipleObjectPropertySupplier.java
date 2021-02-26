package ru.tinkoff.qa.neptune.core.api.properties.object;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;

import static java.lang.Class.forName;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * This interface is designed to read properties and return lists of instances.
 * Each instance is created by by invocation of {@link Supplier#get()}.
 * Resulted list of objects is used as value of the property.
 * The correct format of the property value is a  comma-separated string
 * that consists of fully qualified names of classes of {@link Supplier}.
 *
 * <p>NOTE!</p>
 * <p></p>
 * Implementor of {@link Supplier} should have default constructor or it may have not any declared constructor.
 *
 * @param <R> is a type of of a value of resulted list.
 * @param <T> is a type of a {@link Supplier} implementor.
 */
@SuppressWarnings("unchecked")
public interface MultipleObjectPropertySupplier<R, T extends Supplier<R>> extends PropertySupplier<List<R>, Class<? extends T>[]> {

    @Override
    default List<R> parse(String s) {
        return stream(s.split(",")).map(s1 -> {
            try {
                var clazz = ((Class<T>) forName(s1));
                var c = clazz.getConstructor();
                c.setAccessible(true);
                return c.newInstance().get();
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(format("Class %s is not found", s1), e);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(format("Class %s has no default constructor", s1), e);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new IllegalArgumentException(format("It is impossible to create an instance of %s for some reason", s1), e);
            }
        }).collect(toList());
    }

    @Override
    default String readValuesToSet(Class<? extends T>... value) {
        return stream(value).map(Class::getName).collect(joining(","));
    }
}
