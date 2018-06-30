package com.github.toy.constructor.core.api.event.firing;

/**
 * This interface is designed to support the logging|reporting.
 */
public interface EventLogger {

    /**
     * Fires the starting of some event.
     *
     * @param message that describes the event.
     */
    void fireTheEventStarting(String message);

    /**
     * Fires the thrown exception.
     *
     * @param throwable which is thrown.
     */
    void fireThrownException(Throwable throwable);

    /**
     * Fires some value which has been returned.
     *
     * @param returned value that should be fired.
     */
    void fireReturnedValue(Object returned);

    /**
     * Fires the finishing of some event.
     */
    void fireEventFinishing();
}
