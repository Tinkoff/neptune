package com.github.toy.constructor.selenium.properties;

import com.github.toy.constructor.core.api.properties.PropertySupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.*;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.*;
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
     * Returns duration of the waiting for appearance of an alert.
     * When {@code "waiting.alert.time.unit"} or {@code "waiting.alert.time"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    WAITING_ALERT_TIME_DURATION(WAITING_ALERT_TIME_UNIT, WAITING_ALERT_TIME_VALUE),

    /**
     * Returns duration of the waiting for some window.
     * When {@code "waiting.window.time.unit"} or {@code "waiting.window.tim
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    WAITING_WINDOW_TIME_DURATION(WAITING_WINDOW_TIME_UNIT, WAITING_WINDOW_TIME_VALUE),

    /**
     * Returns duration of the waiting for the switching to some frame succeeded.
     * When {@code "waiting.frame.switching.time.unit"} or {@code "waiting.frame.switching.time"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    WAITING_FRAME_SWITCHING_DURATION(WAITING_FRAME_SWITCHING_TIME_UNIT, WAITING_FRAME_SWITCHING_TIME_VALUE);

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
         * @see ChronoUnit
         */
        ELEMENT_WAITING_TIME_UNIT("waiting.for.elements.time.unit"),

        /**
         * Reads property {@code "waiting.alert.time.unit"}.
         * This property is needed to define time of the waiting for appearance
         * of an alert. Returns read value or {@code null} when nothing is defined
         * @see ChronoUnit
         */
        WAITING_ALERT_TIME_UNIT("waiting.alert.time.unit"),

        /**
         * Reads property {@code "waiting.window.time.unit"}.
         * This property is needed to define time of the waiting for some window.
         * Returns read value or {@code null} when nothing is defined
         * @see ChronoUnit
         */
        WAITING_WINDOW_TIME_UNIT("waiting.window.time.unit"),

        /**
         * Reads property {@code "waiting.frame.switching.time.unit"}.
         * This property is needed to define time of the waiting for the switching to some frame succeeded.
         * Returns read value or {@code null} when nothing is defined
         * @see ChronoUnit
         */
        WAITING_FRAME_SWITCHING_TIME_UNIT("waiting.frame.switching.time.unit");

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
            return returnOptionalFromEnvironment()
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
        ELEMENT_WAITING_TIME_VALUE("waiting.for.elements.time"),

        /**
         * Reads property {@code "waiting.alert.time"}.
         * This property is needed to define time of the waiting for appearance
         * of an alert. Returns read value or {@code null} if nothing is defined.
         */
        WAITING_ALERT_TIME_VALUE("waiting.alert.time"),

        /**
         * Reads property {@code "waiting.window.time"}.
         * This property is needed to define time of the waiting for some window.
         * Returns read value or {@code null} if nothing is defined.
         */
        WAITING_WINDOW_TIME_VALUE("waiting.window.time"),

        /**
         * Reads property {@code "waiting.frame.switching.time"}.
         * This property is needed to define time of the waiting for the switching to some frame succeeded.
         * Returns read value or {@code null} if nothing is defined.
         */
        WAITING_FRAME_SWITCHING_TIME_VALUE("waiting.frame.switching.time");

        private final String propertyName;

        TimeValueProperties(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public String getPropertyName() {
            return propertyName;
        }

        @Override
        public Long get() {
            return returnOptionalFromEnvironment()
                    .map(Long::parseLong)
                    .orElse(null);
        }
    }
}
