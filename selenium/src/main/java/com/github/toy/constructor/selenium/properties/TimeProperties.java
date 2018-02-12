package com.github.toy.constructor.selenium.properties;

import com.github.toy.constructor.core.api.PropertySupplier;

import java.util.concurrent.TimeUnit;

import static com.github.toy.constructor.selenium.properties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.TimeUnitProperties.WAITING_TIME_UNIT;
import static java.util.concurrent.TimeUnit.MINUTES;

public enum TimeProperties implements PropertySupplier<Long> {
    /**
     * Reads property {@code "waiting.for.elements.time"}.
     * This property is needed to define time of the waiting for some web elements
     * are present and suit some criteria.
     * Returns read value or something that is equivalent to 1 minute see
     * {{@link TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT}} and {@link TimeUnit#convert(long, TimeUnit)}
     */
    ELEMENT_WAITING_TIME_VALUE("waiting.for.elements.time") {
        public Long get() {
            return returnOptional()
                    .map(Long::parseLong)
                    .orElseGet(() -> {TimeUnit defaultTimeUnit = ELEMENT_WAITING_TIME_UNIT.get();
                        return defaultTimeUnit.convert(1, MINUTES);
                    });
        }
    },

    /**
     * Reads property {@code "waiting.selenium.time"}.
     * This property is needed to define time of the waiting for some
     * conditions related to {@code  org.openqa.selenium.*} objects.
     * Returns read value or something that is equivalent to 1 minute see
     * {{@link TimeUnitProperties#WAITING_TIME_UNIT}} and {@link TimeUnit#convert(long, TimeUnit)}
     */
    WAITING_TIME_VALUE("waiting.selenium.time") {
        public Long get() {
            return returnOptional()
                    .map(Long::parseLong)
                    .orElseGet(() -> {TimeUnit defaultTimeUnit = WAITING_TIME_UNIT.get();
                        return defaultTimeUnit.convert(1, MINUTES);
                    });
        }
    };

    private final String propertyName;

    TimeProperties(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public abstract Long get();
}
