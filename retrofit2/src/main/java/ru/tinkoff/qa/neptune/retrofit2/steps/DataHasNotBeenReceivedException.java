package ru.tinkoff.qa.neptune.retrofit2.steps;

public class DataHasNotBeenReceivedException extends RuntimeException {

    public DataHasNotBeenReceivedException(String message) {
        super(message);
    }

    public DataHasNotBeenReceivedException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
