package ru.tinkoff.qa.neptune.http.api.response;

public class DesiredDataHasNotBeenReceivedException extends RuntimeException {

    public DesiredDataHasNotBeenReceivedException(String message) {
        super(message);
    }

    public DesiredDataHasNotBeenReceivedException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
