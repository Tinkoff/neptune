package ru.tinkoff.qa.neptune.core.api.steps.proxy;

public class ProxyCreationFailureException extends RuntimeException {
    public ProxyCreationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
