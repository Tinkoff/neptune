package ru.tinkoff.qa.neptune.http.api;

public class DesiredResponseHasNotBeenReceivedException extends RuntimeException {
    public DesiredResponseHasNotBeenReceivedException(String message) {
        super(message);
    }

    public DesiredResponseHasNotBeenReceivedException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
