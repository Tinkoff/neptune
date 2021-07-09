package ru.tinkoff.qa.neptune.core.api.event.firing;

import java.util.Map;

/**
 * This interface is designed to support the logging|reporting.
 */
public interface EventLogger {

    /**
     * Fires the starting of some event.
     *
     * @param message that describes the event.
     */
    void fireTheEventStarting(String message, Map<String, String> parameters);

    /**
     * Fires the thrown exception.
     *
     * @param throwable which is thrown.
     */
    void fireThrownException(Throwable throwable);

    /**
     * Fires some value which has been returned.
     *
     * @param resultDescription is a description of returned value
     * @param returned          value that should be fired.
     */
    void fireReturnedValue(String resultDescription, Object returned);

    /**
     * Fires the finishing of some event.
     */
    void fireEventFinishing();

    /**
     * Adds parameters calculated during the step performing.
     *
     * @param parameters additional parameters
     */
    void addParameters(Map<String, String> parameters);
}
