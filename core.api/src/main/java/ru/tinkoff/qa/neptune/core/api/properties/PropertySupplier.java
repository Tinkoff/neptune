package ru.tinkoff.qa.neptune.core.api.properties;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.System.clearProperty;
import static java.lang.System.setProperty;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.substringsBetween;
import static ru.tinkoff.qa.neptune.core.api.properties.PropertySource.isPropertyKeyPresent;

/**
 * Interface to construct classes which read property values
 *
 * @param <T> type of read values
 * @param <R> type of values to set explicitly
 */
public interface PropertySupplier<T, R> extends Supplier<T>, Consumer<R> {

    private static PropertyDefaultValue getPropertyDefaultValue(PropertySupplier<?, ?> supplier) {
        var clz = supplier.getClass().isAnonymousClass() ? supplier.getClass().getSuperclass() : supplier.getClass();

        if (clz.isEnum()) {
            try {
                return clz.getDeclaredField(((Enum<?>) supplier).name()).getAnnotation(PropertyDefaultValue.class);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } else {
            return clz.getAnnotation(PropertyDefaultValue.class);
        }
    }

    static Optional<String> returnOptionalFromEnvironment(PropertySupplier<?, ?> property) {
        return ofNullable(property.getPropertyValue());
    }

    private static String preparePropertyValue(String rawValue) {
        var patterns = substringsBetween(rawValue, "${", "}");
        if (isNull(patterns) || patterns.length == 0) {
            return rawValue;
        }

        var result = rawValue;
        for (var p : patterns) {
            var propertyValue = PropertySource.getPropertyValue(p);
            if (isBlank(propertyValue)) {
                throw new IllegalStateException(p + " is not defined");
            }
            result = result.replace("${" + p + "}", propertyValue);
        }

        return result;
    }

    private String getPropertyValue() {
        var property = getName();

        var value = PropertySource.getPropertyValue(property);
        if (isBlank(value)) {
            return null;
        }

        return preparePropertyValue(value);
    }

    /**
     * Converts object to string value of the property
     *
     * @param value to set as a value of the property
     * @return value converted to String
     */
    default String readValuesToSet(R value) {
        return value.toString();
    }

    /**
     * Sets a new value of some system property.
     * <p></p>
     * WARNING!!! When there are some additional {@link PropertySource}'s and they provide a value of
     * the same property then the accepted value is ignored in favor of externally provided.
     *
     * @param value is the new value
     */
    default void accept(R value) {
        var name = getName();
        ofNullable(value)
                .ifPresentOrElse(r -> setProperty(name, readValuesToSet(r)),
                        () -> clearProperty(name));
    }

    default String getName() {
        var clz = this.getClass().isAnonymousClass() ? this.getClass().getSuperclass() : this.getClass();

        PropertyName propertyName;
        if (clz.isEnum()) {
            try {
                propertyName = clz.getDeclaredField(((Enum<?>) this).name()).getAnnotation(PropertyName.class);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } else {
            propertyName = clz.getAnnotation(PropertyName.class);
        }


        return ofNullable(propertyName)
                .map(PropertyName::value)
                .orElseThrow(() -> {
                    if (!clz.isEnum()) {
                        return new IllegalArgumentException(format("The class %s is not annotated by %s",
                                clz.getSimpleName(),
                                PropertyName.class.getSimpleName()));
                    }

                    return new IllegalArgumentException(format("The field %s the enum %s is not annotated by %s",
                            ((Enum<?>) this).name(),
                            clz.getSimpleName(),
                            PropertyName.class.getSimpleName()));
                });
    }

    @Override
    default T get() {
        var thisRef = this;
        return returnOptionalFromEnvironment(this)
                .map(this::parse)
                .orElseGet(() -> {
                    if (isPropertyKeyPresent(getName())) {
                        return returnIfNull();
                    }

                    return ofNullable(getPropertyDefaultValue(thisRef))
                            .map(v -> parse(v.value()))
                            .orElseGet(this::returnIfNull);
                });
    }

    T parse(String value);

    default T returnIfNull() {
        return null;
    }
}
