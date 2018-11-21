package ru.tinkoff.qa.neptune.http.api;

public class ResponseHasNoDesiredDataException extends RuntimeException {

    public ResponseHasNoDesiredDataException(String message) {
        super(message);
    }

    public ResponseHasNoDesiredDataException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
