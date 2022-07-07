package ru.tinkoff.qa.neptune.core.api.properties.enums;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.reflect.TypeToken.of;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.*;

/**
 * This interface is designed to read properties and return lists of constants declared by enums.
 * The valid format of property value is a comma-separated string.
 *
 * @param <T> is a type of enum.
 */
public interface MultipleEnumPropertySuppler<T extends Enum<?>> extends PropertySupplier<List<T>, List<T>> {

    @SuppressWarnings("unchecked")
    @Override
    default List<T> parse(String value) {
        var names = stream(value.split(",")).map(String::trim)
                .toArray(String[]::new);

        Class<?> cls = this.getClass();
        Type[] interfaces;
        Type enumSupplier;
        while ((interfaces = cls.getGenericInterfaces()).length == 0 || (enumSupplier = stream(interfaces)
                .filter(type -> MultipleEnumPropertySuppler.class.isAssignableFrom(of(type).getRawType()))
                .findFirst()
                .orElse(null)) == null) {
            cls = cls.getSuperclass();
        }

        var enumType = (Class<T>) ((ParameterizedType) enumSupplier).getActualTypeArguments()[0];

        var valueMap = stream(enumType.getEnumConstants())
                .collect(toMap(Enum::name, o -> o));

        var result = stream(names)
                .map(s -> ofNullable(valueMap.get(s)).orElseThrow(() ->
                        new IllegalArgumentException(format("Unknown constant %s from enum %s", s, enumType.getName()))))
                .distinct()
                .collect(toList());

        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    default String readValuesToSet(List<T> value) {
        return value.stream().map(Enum::name).collect(joining(","));
    }
}
