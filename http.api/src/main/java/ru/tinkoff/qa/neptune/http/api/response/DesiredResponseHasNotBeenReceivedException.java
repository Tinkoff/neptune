package ru.tinkoff.qa.neptune.http.api.response;

public class DesiredResponseHasNotBeenReceivedException extends RuntimeException {

    public DesiredResponseHasNotBeenReceivedException(String message) {
        super(message);
    }
}
