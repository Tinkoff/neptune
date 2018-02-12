package com.github.toy.constructor.selenium.properties;

import com.github.toy.constructor.core.api.PropertySupplier;

import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.concurrent.TimeUnit.SECONDS;

public enum  TimeUnitProperties implements PropertySupplier<TimeUnit> {
    /**
     * Reads property {@code "waiting.for.elements.time.unit"}.
     * This property is needed to define time of the waiting for some web elements
     * are present and suit some criteria. Returns read value
     * or {@link TimeUnit#SECONDS} when nothing is defined
     */
    ELEMENT_WAITING_TIME_UNIT("waiting.for.elements.time.unit"),

    /**
     * Reads property {@code "waiting.selenium.time.unit"}.
     * This property is needed to define time of the waiting for some
     * conditions related to {@code  org.openqa.selenium.*} objects.
     * Returns read value or {@link TimeUnit#SECONDS} when nothing is defined
     */
    WAITING_TIME_UNIT("waiting.selenium.time.unit");;

    private final String propertyName;

    TimeUnitProperties(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public TimeUnit get() {
        return returnOptional()
                .map(s -> stream(TimeUnit.values())
                        .filter(timeUnit -> s.trim().equalsIgnoreCase(timeUnit.name()))
                        .findFirst()
                        .orElseThrow(
                                () -> new IllegalArgumentException(format("Property: %s. Unidentified " +
                                                "time unit %s. Please take a look at " +
                                                "elements of %s", this.toString(), s,
                                        TimeUnit.class.getName())))).orElse(SECONDS);
    }
}
