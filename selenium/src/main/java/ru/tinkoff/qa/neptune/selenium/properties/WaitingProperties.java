package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

import java.time.temporal.ChronoUnit;

import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.*;

public final class WaitingProperties extends DurationSupplier {
    /**
     * Returns duration of the waiting for some web elements
     * are present and suit some criteria. When {@code "WAITING_FOR_ELEMENTS_TIME_UNIT"} or
     * {@code "WAITING_FOR_ELEMENTS_TIME"} are not defined then it returns 1 minute.
     * Otherwise it returns defined duration value.
     */
    public static final WaitingProperties ELEMENT_WAITING_DURATION = new WaitingProperties(ELEMENT_WAITING_TIME_UNIT, ELEMENT_WAITING_TIME_VALUE);

    /**
     * Returns duration of the waiting for appearance of an alert.
     * When {@code "WAITING_FOR_ALERTS_TIME_UNIT"} or {@code "WAITING_FOR_ALERTS_TIME"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    public static final WaitingProperties WAITING_ALERT_TIME_DURATION = new WaitingProperties(WAITING_ALERT_TIME_UNIT, WAITING_ALERT_TIME_VALUE);

    /**
     * Returns duration of the waiting for some window.
     * When {@code "WAITING_FOR_WINDOWS_TIME_UNIT"} or {@code "WAITING_FOR_WINDOWS_TIME"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    public static final WaitingProperties WAITING_WINDOW_TIME_DURATION = new WaitingProperties(WAITING_WINDOW_TIME_UNIT, WAITING_WINDOW_TIME_VALUE);

    /**
     * Returns duration of the waiting for the switching to some frame succeeded.
     * When {@code "WAITING_FOR_FRAME_SWITCHING_TIME_UNIT"} or {@code "WAITING_FOR_FRAME_SWITCHING_TIME"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    public static final WaitingProperties WAITING_FRAME_SWITCHING_DURATION = new WaitingProperties(WAITING_FRAME_SWITCHING_TIME_UNIT, WAITING_FRAME_SWITCHING_TIME_VALUE);

    /**
     * Returns duration of the waiting for waiting for a page is loaded.
     * When {@code "WAITING_FOR_PAGE_LOADED_TIME_UNIT"} or {@code "WAITING_FOR_PAGE_LOADED_TIME"}
     * are not defined then it returns 1 minute. Otherwise it returns defined duration value.
     */
    public static final WaitingProperties WAITING_FOR_PAGE_LOADED_DURATION = new WaitingProperties(WAITING_FOR_PAGE_LOADED_TIME_UNIT, WAITING_FOR_PAGE_LOADED_TIME_VALUE);

    private WaitingProperties(TimeUnitProperties durationUnitPropertySupplier,
                              TimeValueProperties durationValuePropertySupplier) {
        super(durationUnitPropertySupplier, durationValuePropertySupplier);
    }


    public enum TimeUnitProperties implements EnumPropertySuppler<ChronoUnit> {
        @PropertyDescription(description = {"This property needs to define time of the waiting for some web elements",
                "are present and suit some criteria",
                "See java.time.temporal.ChronoUnit"},
                section = "Selenium. Waiting for elements.")
        @PropertyName("WAITING_FOR_ELEMENTS_TIME_UNIT")
        @PropertyDefaultValue("MINUTES")
        ELEMENT_WAITING_TIME_UNIT,

        @PropertyDescription(description = {"This property needs to define time of the waiting for appearance of an alert",
                "See java.time.temporal.ChronoUnit"},
                section = "Selenium. Waiting for alerts.")
        @PropertyName("WAITING_FOR_ALERTS_TIME_UNIT")
        @PropertyDefaultValue("MINUTES")
        WAITING_ALERT_TIME_UNIT,

        /**
         * Reads property {@code "WAITING_FOR_WINDOWS_TIME_UNIT"}.
         * This property is needed to define time of the waiting for some window.
         * Returns read value or {@code null} when nothing is defined
         *
         * @see ChronoUnit
         */
        @PropertyDescription(description = {"This property needs to define time of the waiting for appearance of a browser window/tab",
                "See java.time.temporal.ChronoUnit"},
                section = "Selenium. Waiting for windows.")
        @PropertyName("WAITING_FOR_WINDOWS_TIME_UNIT")
        @PropertyDefaultValue("MINUTES")
        WAITING_WINDOW_TIME_UNIT,

        @PropertyDescription(description = {"This property needs to define time of the waiting for the switching to some frame succeeded",
                "See java.time.temporal.ChronoUnit"},
                section = "Selenium. Waiting for frames.")
        @PropertyName("WAITING_FOR_FRAME_SWITCHING_TIME_UNIT")
        @PropertyDefaultValue("MINUTES")
        WAITING_FRAME_SWITCHING_TIME_UNIT,

        @PropertyDescription(description = {"This property needs to define time of the waiting for a page is loaded",
                "See java.time.temporal.ChronoUnit"},
                section = "Selenium. Waiting for page is loaded.")
        @PropertyName("WAITING_FOR_PAGE_LOADED_TIME_UNIT")
        @PropertyDefaultValue("MINUTES")
        WAITING_FOR_PAGE_LOADED_TIME_UNIT
    }

    public enum TimeValueProperties implements LongValuePropertySupplier {
        @PropertyDescription(description = {"This property needs to define time of the waiting for some web elements",
                "are present and suit some criteria"},
                section = "Selenium. Waiting for elements.")
        @PropertyName("WAITING_FOR_ELEMENTS_TIME")
        @PropertyDefaultValue("1")
        ELEMENT_WAITING_TIME_VALUE,

        @PropertyDescription(description = "This property needs to define time of the waiting for appearance of an alert",
                section = "Selenium. Waiting for alerts.")
        @PropertyName("WAITING_FOR_ALERTS_TIME")
        @PropertyDefaultValue("1")
        WAITING_ALERT_TIME_VALUE,

        @PropertyDescription(description = "This property needs to define time of the waiting for appearance of a browser window/tab",
                section = "Selenium. Waiting for windows.")
        @PropertyName("WAITING_FOR_WINDOWS_TIME")
        @PropertyDefaultValue("1")
        WAITING_WINDOW_TIME_VALUE,

        /**
         * Reads property {@code "WAITING_FOR_FRAME_SWITCHING_TIME"}.
         * This property is needed to define time of the waiting for the switching to some frame succeeded.
         * Returns read value or {@code null} if nothing is defined.
         */
        @PropertyDescription(description = "This property needs to define time of the waiting for the switching to some frame succeeded",
                section = "Selenium. Waiting for frames.")
        @PropertyName("WAITING_FOR_FRAME_SWITCHING_TIME")
        @PropertyDefaultValue("1")
        WAITING_FRAME_SWITCHING_TIME_VALUE,


        @PropertyDescription(description = "This property needs to define time of the waiting for a page is loaded",
                section = "Waiting for page is loaded.")
        @PropertyName("WAITING_FOR_PAGE_LOADED_TIME")
        @PropertyDefaultValue("1")
        WAITING_FOR_PAGE_LOADED_TIME_VALUE
    }
}
