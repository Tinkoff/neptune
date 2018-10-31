package ru.tinkoff.qa.neptune.core.api.properties.enums;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.google.common.reflect.TypeToken.of;
import static java.lang.String.format;
import static java.util.Arrays.stream;

/**
 * This interface is designed to read properties and return single constants declared by enums.
 *
 * @param <T> is a type of enum.
 */
public interface EnumPropertySuppler<T extends Enum> extends PropertySupplier<T> {

    @SuppressWarnings("unchecked")
    private T findValue(String name) {
        Class<?> cls = this.getClass();
        Type[] interfaces;
        Type enumSupplier;
        while ((interfaces = cls.getGenericInterfaces()).length == 0 || (enumSupplier = stream(interfaces)
                .filter(type -> EnumPropertySuppler.class.isAssignableFrom(of(type).getRawType()))
                .findFirst()
                .orElse(null)) == null) {
            cls = cls.getSuperclass();
        }

        var enumType = ((Class<T>) ((ParameterizedType)
                enumSupplier).getActualTypeArguments()[0]);

        return stream(enumType.getEnumConstants()).filter(t -> name.trim().equals(t.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(format("Unknown constant %s from enum %s", name, enumType.getName())));
    }

    /**
     * This method reads value of the property and converts it to a constant declared by some enum.
     *
     * @return some enum constant.
     */
    default T get() {
        return returnOptionalFromEnvironment().map(this::findValue).orElse(null);
    }
}
