package com.github.toy.constructor.selenium.properties;

import com.github.toy.constructor.core.api.PropertySupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_TIME_VALUE;
import static java.lang.String.format;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Arrays.stream;

public enum WaitingProperties implements Supplier<Duration> {
    /**
     * Returns duration of the waiting for some web elements
     * are present and suit some criteria. When {@code "waiting.for.elements.time.unit"} or
     * {@code "waiting.for.elements.time"} are not defined then it returns 1 minute.
     * Otherwise it returns defined duration value.
     */
    ELEMENT_WAITING_DURATION(ELEMENT_WAITING_TIME_UNIT, ELEMENT_WAITING_TIME_VALUE),

    /**
     * Returns duration of the waiting for some
     * conditions related to {@code  org.openqa.selenium.*} objects.
     * When {@code "waiting.selenium.time.unit"} or {@code "waiting.selenium.time"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    WAITING_TIME_DURATION(WAITING_TIME_UNIT, WAITING_TIME_VALUE);

    private final TimeUnitProperties timeUnit;
    private final TimeValueProperties timeValue;

    WaitingProperties(TimeUnitProperties timeUnit, TimeValueProperties timeValue) {
        this.timeUnit = timeUnit;
        this.timeValue = timeValue;
    }

    @Override
    public Duration get() {
        if (timeUnit.get() == null || timeValue.get() == null) {
            return of(1, MINUTES);
        }
        return of(timeValue.get(), timeUnit.get());
    }

    public enum TimeUnitProperties implements PropertySupplier<ChronoUnit> {
        /**
         * Reads property {@code "waiting.for.elements.time.unit"}.
         * This property is needed to define time of the waiting for some web elements
         * are present and suit some criteria. Returns read value
         * or {@code null} when nothing is defined
         */
        ELEMENT_WAITING_TIME_UNIT("waiting.for.elements.time.unit"),

        /**
         * Reads property {@code "waiting.selenium.time.unit"}.
         * This property is needed to define time of the waiting for some
         * conditions related to {@code  org.openqa.selenium.*} objects.
         * Returns read value or {@code null} when nothing is defined
         */
        WAITING_TIME_UNIT("waiting.selenium.time.unit");

        private final String propertyName;

        TimeUnitProperties(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public String getPropertyName() {
            return propertyName;
        }

        @Override
        public ChronoUnit get() {
            return returnOptional()
                    .map(s -> stream(ChronoUnit.values())
                            .filter(timeUnit -> s.trim().equalsIgnoreCase(timeUnit.name()))
                            .findFirst()
                            .orElseThrow(
                                    () -> new IllegalArgumentException(format("Property: %s. Unidentified " +
                                                    "time unit %s. Please take a look at " +
                                                    "elements of %s", this.toString(), s,
                                            ChronoUnit.class.getName())))).orElse(null);
        }
    }

    public enum TimeValueProperties implements PropertySupplier<Long> {
        /**
         * Reads property {@code "waiting.for.elements.time"}.
         * This property is needed to define time of the waiting for some web elements
         * are present and suit some criteria.
         * Returns read value or {@code null} if nothing is defined.
         */
        ELEMENT_WAITING_TIME_VALUE("waiting.for.elements.time") {
            public Long get() {
                return returnOptional()
                        .map(Long::parseLong)
                        .orElse(null);
            }
        },

        /**
         * Reads property {@code "waiting.selenium.time"}.
         * This property is needed to define time of the waiting for some
         * conditions related to {@code  org.openqa.selenium.*} objects.
         * Returns read value or {@code null} if nothing is defined.
         */
        WAITING_TIME_VALUE("waiting.selenium.time") {
            public Long get() {
                return returnOptional()
                        .map(Long::parseLong)
                        .orElse(null);
            }
        };

        private final String propertyName;

        TimeValueProperties(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public String getPropertyName() {
            return propertyName;
        }

        @Override
        public abstract Long get();
    }
}
