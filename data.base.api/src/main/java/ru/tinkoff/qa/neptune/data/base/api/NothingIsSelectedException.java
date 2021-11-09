package ru.tinkoff.qa.neptune.data.base.api;

@Deprecated(forRemoval = true)
public class NothingIsSelectedException extends RuntimeException {
    public NothingIsSelectedException(String message) {
        super(message);
    }
}
