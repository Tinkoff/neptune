package ru.tinkoff.qa.neptune.core.api.properties;

import static ru.tinkoff.qa.neptune.core.api.properties.CapturedEvents.FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.CapturedEvents.SUCCESS;
import static ru.tinkoff.qa.neptune.core.api.properties.CapturedEvents.SUCCESS_AND_FAILURE;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

/**
 * This class is designed to read the property {@code "do.captures.of"} and to return
 * an element of {@link CapturedEvents}.
 */
public final class DoCapturesOf implements PropertySupplier<CapturedEvents> {

    private final static String DO_CAPTURES_OF = "do.captures.of";
    public final static DoCapturesOf DO_CAPTURES_OF_INSTANCE = new DoCapturesOf();

    private DoCapturesOf() {
        super();
    }

    /**
     * Should be success events captured or not.
     *
     * @return {@code true} of the property {@code "do.captures.of"} is
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
     * @return {@code true} of the property {@code "do.captures.of"} is
     * {@link CapturedEvents#FAILURE} or {@link CapturedEvents#SUCCESS_AND_FAILURE}. {@code false}
     * is returned otherwise.
     */
    public static boolean catchFailureEvent() {
        return ofNullable(DO_CAPTURES_OF_INSTANCE.get())
                .map(capturedEvents -> FAILURE.equals(capturedEvents)
                        || SUCCESS_AND_FAILURE.equals(capturedEvents))
                .orElse(false);
    }

    @Override
    public String getPropertyName() {
        return DO_CAPTURES_OF;
    }

    @Override
    public CapturedEvents get() {
        return returnOptionalFromEnvironment()
                .map(s -> stream(CapturedEvents.values())
                        .filter(capturedEvents -> s.trim().equals(capturedEvents.name()))
                        .findFirst().orElse(null))
                .orElse(null);
    }
}
