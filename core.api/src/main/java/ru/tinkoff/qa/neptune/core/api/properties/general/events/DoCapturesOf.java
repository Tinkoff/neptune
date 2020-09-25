package ru.tinkoff.qa.neptune.core.api.properties.general.events;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;

@PropertyDescription(description = "Defines events to make log attachments. Available values: SUCCESS, FAILURE, SUCCESS_AND_FAILURE",
        section = "General properties. Report captures/Attachments")
@PropertyName("DO_CAPTURES_OF")
public final class DoCapturesOf implements EnumPropertySuppler<CapturedEvents> {
    public final static DoCapturesOf DO_CAPTURES_OF_INSTANCE = new DoCapturesOf();

    private DoCapturesOf() {
        super();
    }

    /**
     * Should be success events captured or not.
     *
     * @return {@code true} of the property {@code "DO_CAPTURES_OF"} is
     * {@link CapturedEvents#SUCCESS} or {@link CapturedEvents#SUCCESS_AND_FAILURE}. {@code false}
     * is returned otherwise.
     */
    public static boolean catchSuccessEvent() {
        return ofNullable(DO_CAPTURES_OF_INSTANCE.get())
                .map(capturedEvents -> SUCCESS.equals(capturedEvents)
                        || SUCCESS_AND_FAILURE.equals(capturedEvents))
                .orElse(false);
    }

    /**
     * Should be failure events captured or not.
     *
     * @return {@code true} of the property {@code "DO_CAPTURES_OF"} is
     * {@link CapturedEvents#FAILURE} or {@link CapturedEvents#SUCCESS_AND_FAILURE}. {@code false}
     * is returned otherwise.
     */
    public static boolean catchFailureEvent() {
        return ofNullable(DO_CAPTURES_OF_INSTANCE.get())
                .map(capturedEvents -> FAILURE.equals(capturedEvents)
                        || SUCCESS_AND_FAILURE.equals(capturedEvents))
                .orElse(false);
    }
}
