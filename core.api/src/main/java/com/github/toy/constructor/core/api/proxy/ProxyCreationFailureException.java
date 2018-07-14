package com.github.toy.constructor.core.api.proxy;

public class ProxyCreationFailureException extends RuntimeException {

    ProxyCreationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
