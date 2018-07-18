package ru.tinkoff.qa.neptune.core.api.proxy;

class ProxyCreationFailureException extends RuntimeException {

    ProxyCreationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
