package ru.tinkoff.qa.neptune.data.base.api.query;

public class NothingIsSelectedException extends RuntimeException {
    public NothingIsSelectedException(String message) {
        super(message);
    }
}
