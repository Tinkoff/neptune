package com.github.toy.constructor.core.api;

public interface Captor<T> {
    /**
     * Does captures of objects and messages. This method is designed for
     * the logging/reporting generally.
     *
     * @param caught is an object to be caught for the logging/reporting
     * @param message to be caught for the logging/reporting
     */
    void doCapture(T caught, String message);
}
