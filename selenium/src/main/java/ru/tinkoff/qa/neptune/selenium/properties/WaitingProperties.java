package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;
import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.*;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.MINUTES;

public final class WaitingProperties extends DurationSupplier {
    /**
     * Returns duration of the waiting for some web elements
     * are present and suit some criteria. When {@code "waiting.for.elements.time.unit"} or
     * {@code "waiting.for.elements.time"} are not defined then it returns 1 minute.
     * Otherwise it returns defined duration value.
     */
    public static final WaitingProperties ELEMENT_WAITING_DURATION = new WaitingProperties(ELEMENT_WAITING_TIME_UNIT,
            ELEMENT_WAITING_TIME_VALUE, of(1, MINUTES));

    /**
     * Returns duration of the waiting for appearance of an alert.
     * When {@code "waiting.alert.time.unit"} or {@code "waiting.alert.time"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    public static final WaitingProperties WAITING_ALERT_TIME_DURATION = new WaitingProperties(WAITING_ALERT_TIME_UNIT,
            WAITING_ALERT_TIME_VALUE, of(1, MINUTES));

    /**
     * Returns duration of the waiting for some window.
     * When {@code "waiting.window.time.unit"} or {@code "waiting.window..time"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    public static final WaitingProperties WAITING_WINDOW_TIME_DURATION = new WaitingProperties(WAITING_WINDOW_TIME_UNIT,
            WAITING_WINDOW_TIME_VALUE, of(1, MINUTES));

    /**
     * Returns duration of the waiting for the switching to some frame succeeded.
     * When {@code "waiting.frame.switching.time.unit"} or {@code "waiting.frame.switching.time"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    public static final WaitingProperties WAITING_FRAME_SWITCHING_DURATION = new WaitingProperties(WAITING_FRAME_SWITCHING_TIME_UNIT,
            WAITING_FRAME_SWITCHING_TIME_VALUE, of(1, MINUTES));

    /**
     * Returns duration of the waiting for waiting for a page is loaded.
     * When {@code "waiting.for.page.loaded.time.unit"} or {@code "waiting.for.page.loaded.time"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    public static final WaitingProperties WAITING_FOR_PAGE_LOADED_DURATION = new WaitingProperties(WAITING_FOR_PAGE_LOADED_TIME_UNIT,
            WAITING_FOR_PAGE_LOADED_TIME_VALUE, of(1, MINUTES));

    private WaitingProperties(TimeUnitProperties durationUnitPropertySupplier,
                              TimeValueProperties durationValuePropertySupplier,
                              Duration returnWhenNotDefined) {
        super(durationUnitPropertySupplier, durationValuePropertySupplier, returnWhenNotDefined);
    }


    public enum TimeUnitProperties implements EnumPropertySuppler<ChronoUnit> {
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

    public enum TimeValueProperties implements LongValuePropertySupplier {
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
