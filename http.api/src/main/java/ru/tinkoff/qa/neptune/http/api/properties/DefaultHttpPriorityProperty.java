package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.integers.IntValuePropertySupplier;

/**
 * This class is designed to read value of the property {@code 'default.http.priority'} and convert it to integer value.
 * The value provided must be between {@code 1} and {@code 256} (inclusive).
 */
public final class DefaultHttpPriorityProperty implements IntValuePropertySupplier {

    private static final String PROPERTY = "default.http.priority";

    /**
     * This instance reads value of the property {@code 'default.http.priority'} and converts it to integer value.
     * The value provided must be between {@code 1} and {@code 256} (inclusive).
     */
    public static final DefaultHttpPriorityProperty DEFAULT_HTTP_PRIORITY_PROPERTY = new DefaultHttpPriorityProperty();

    private DefaultHttpPriorityProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }
}
