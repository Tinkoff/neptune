package com.github.toy.constructor.core.api;

public interface ReportLogger<T> {
    /**
     * Simple logging of a message.
     * @param message to be logged
     */
    void log(T objectToLog, String message);
}
