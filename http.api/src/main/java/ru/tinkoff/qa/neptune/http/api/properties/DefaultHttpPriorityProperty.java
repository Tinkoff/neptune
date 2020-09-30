package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.integers.IntValuePropertySupplier;

/**
 * This class is designed to read value of the property {@code 'DEFAULT_HTTP_COOKIE_EXECUTOR'} and convert it to integer value.
 * The value provided must be between {@code 1} and {@code 256} (inclusive).
 */
@PropertyDescription(description = "Defines default priority for any HTTP/2 requests",
        section = "Http client. General")
@PropertyName("DEFAULT_HTTP_PRIORITY")
public final class DefaultHttpPriorityProperty implements IntValuePropertySupplier {

    /**
     * This instance reads value of the property {@code 'DEFAULT_HTTP_PRIORITY'} and converts it to integer value.
     * The value provided must be between {@code 1} and {@code 256} (inclusive).
     */
    public static final DefaultHttpPriorityProperty DEFAULT_HTTP_PRIORITY_PROPERTY = new DefaultHttpPriorityProperty();

    private DefaultHttpPriorityProperty() {
        super();
    }
}
