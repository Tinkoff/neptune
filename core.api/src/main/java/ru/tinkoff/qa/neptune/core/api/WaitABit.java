package ru.tinkoff.qa.neptune.core.api;

import java.time.Duration;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Thread.sleep;
import static java.util.Objects.nonNull;

/**
 * This class is designed to suspend thread execution for sometime instead of {@link Thread#sleep(long)}
 */
public final class WaitABit {

    private WaitABit() {
        super();
    }

    /**
     * This method suspends thread execution for a time defined in milliseconds.
     * @param millis is a time of thread suspending
     */
    public static void waitABit(long millis) {
        try {
            sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * This method suspends thread execution for a time defined as a duration value.
     * @param duration is a time of thread suspending
     */
    public static void waitABit(Duration duration) {
        checkArgument(nonNull(duration), "Duration value should differ from null");
        waitABit(duration.toMillis());
    }
}
