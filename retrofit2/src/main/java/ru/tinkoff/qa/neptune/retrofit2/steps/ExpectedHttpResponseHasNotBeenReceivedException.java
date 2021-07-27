package ru.tinkoff.qa.neptune.retrofit2.steps;

public final class ExpectedHttpResponseHasNotBeenReceivedException extends RuntimeException {

    public ExpectedHttpResponseHasNotBeenReceivedException(String message) {
        super(message);
    }

    public ExpectedHttpResponseHasNotBeenReceivedException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
