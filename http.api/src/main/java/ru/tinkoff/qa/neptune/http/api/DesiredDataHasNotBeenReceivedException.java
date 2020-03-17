package ru.tinkoff.qa.neptune.http.api;

public class DesiredDataHasNotBeenReceivedException extends RuntimeException {

    public DesiredDataHasNotBeenReceivedException(String message) {
        super(message);
    }

    public DesiredDataHasNotBeenReceivedException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
