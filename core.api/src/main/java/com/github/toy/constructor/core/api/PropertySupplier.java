package com.github.toy.constructor.core.api;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.lang.System.setProperty;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Interface to construct classes which read property values
 * @param <T> type of read values
 */
public interface PropertySupplier<T> extends Supplier<T>, Consumer<String> {

    default Optional<String> returnOptional() {
        return ofNullable(getPropertyValue());
    }

    private String getPropertyValue() {
        String property = getPropertyName();
        return ofNullable(System.getProperty(property))
                .orElseGet(() -> System.getenv(property));
    }

    /**
     * This method is supposed to return some property name
     * @return name of some property
     */
    String getPropertyName();

    /**
     * Sets a new value of some system property
     * @param value is the new value
     */
    default void accept(String value) {
        checkArgument(!isBlank(value), format("New value of the '%s' should not be blank", getPropertyName()));
        setProperty(getPropertyName(), value);
    }
}
