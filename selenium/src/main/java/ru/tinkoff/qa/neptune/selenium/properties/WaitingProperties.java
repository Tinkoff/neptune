package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.waiting.time.DurationSupplier;
import ru.tinkoff.qa.neptune.core.api.properties.waiting.time.DurationUnitPropertySupplier;
import ru.tinkoff.qa.neptune.core.api.properties.waiting.time.DurationValuePropertySupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.*;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.MINUTES;

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
     * When {@code "waiting.window.time.unit"} or {@code "waiting.window..time"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    WAITING_WINDOW_TIME_DURATION(WAITING_WINDOW_TIME_UNIT, WAITING_WINDOW_TIME_VALUE),

    /**
     * Returns duration of the waiting for the switching to some frame succeeded.
     * When {@code "waiting.frame.switching.time.unit"} or {@code "waiting.frame.switching.time"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    WAITING_FRAME_SWITCHING_DURATION(WAITING_FRAME_SWITCHING_TIME_UNIT, WAITING_FRAME_SWITCHING_TIME_VALUE),

    /**
     * Returns duration of the waiting for waiting for a page is loaded.
     * When {@code "waiting.for.page.loaded.time.unit"} or {@code "waiting.for.page.loaded.time"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    WAITING_FOR_PAGE_LOADED_DURATION(WAITING_FOR_PAGE_LOADED_TIME_UNIT, WAITING_FOR_PAGE_LOADED_TIME_VALUE);

    private final DurationSupplier durationSupplier;

    WaitingProperties(TimeUnitProperties timeUnit, TimeValueProperties timeValue) {
        durationSupplier = new DurationSupplier(timeUnit, timeValue) {
            @Override
            public Duration get() {
                return getDuration(of(1, MINUTES));
            }
        };
    }

    @Override
    public Duration get() {
        return durationSupplier.get();
    }

    public enum TimeUnitProperties implements DurationUnitPropertySupplier {
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
        WAITING_FRAME_SWITCHING_TIME_UNIT("waiting.frame.switching.time.unit"),

        /**
         * Reads property {@code "waiting.for.page.loaded.time.unit"}.
         * This property is needed to define time of the waiting for a page is loaded.
         * Returns read value or {@code null} when nothing is defined
         * @see ChronoUnit
         */
        WAITING_FOR_PAGE_LOADED_TIME_UNIT("waiting.for.page.loaded.time.unit");

        private final String propertyName;

        TimeUnitProperties(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public String getPropertyName() {
            return propertyName;
        }
    }

    public enum TimeValueProperties implements DurationValuePropertySupplier {
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
        WAITING_FRAME_SWITCHING_TIME_VALUE("waiting.frame.switching.time"),

        /**
         * Reads property {@code "waiting.for.page.loaded.time"}.
         * This property is needed to define time of the waiting for  a page is loaded.
         * Returns read value or {@code null} if nothing is defined.
         */
        WAITING_FOR_PAGE_LOADED_TIME_VALUE("waiting.for.page.loaded.time");

        private final String propertyName;

        TimeValueProperties(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public String getPropertyName() {
            return propertyName;
        }
    }
}
