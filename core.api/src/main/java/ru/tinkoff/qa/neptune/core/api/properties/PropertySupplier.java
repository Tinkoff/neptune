package ru.tinkoff.qa.neptune.core.api.properties;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.lang.System.setProperty;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.arePropertiesRead;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.refreshProperties;

/**
 * Interface to construct classes which read property values
 *
 * @param <T> type of read values
 */
public interface PropertySupplier<T> extends Supplier<T>, Consumer<String> {

    private static PropertyDefaultValue getPropertyDefaultValue(PropertySupplier<?> supplier) {
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

    private Optional<String> returnOptionalFromEnvironment(String property) {
        return ofNullable(getPropertyValue(property));
    }

    private String getPropertyValue(String property) {
        if (!arePropertiesRead()) {
            refreshProperties();
        }

        var value = System.getenv(property);
        if (isBlank(value)) {
            value = System.getProperty(property);
        }

        if (isNotBlank(value)) {
            return value;
        }

        return null;
    }

    private boolean isPropertyDefined(String property) {
        return System.getenv().containsKey(property) || System.getProperties().containsKey(property);
    }

    /**
     * Sets a new value of some system property
     *
     * @param value is the new value
     */
    default void accept(String value) {
        var name = getName();
        checkArgument(!isBlank(value), format("New value of the '%s' should not be blank", name));
        setProperty(name, value);
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
        var property = getName();
        return returnOptionalFromEnvironment(property)
                .map(this::parse)
                .orElseGet(() -> {
                    if (isPropertyDefined(property)) {
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
