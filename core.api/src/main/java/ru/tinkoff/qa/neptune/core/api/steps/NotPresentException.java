package ru.tinkoff.qa.neptune.core.api.steps;

/**
 * Default exception which may be thrown when get-step returns no valuable data.
 */
public final class NotPresentException extends RuntimeException {

    public NotPresentException(String message) {
        super(message);
    }

    public NotPresentException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
