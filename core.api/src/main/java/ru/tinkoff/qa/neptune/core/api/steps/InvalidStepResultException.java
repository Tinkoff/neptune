package ru.tinkoff.qa.neptune.core.api.steps;

public class InvalidStepResultException extends RuntimeException {

    public InvalidStepResultException(String message) {
        super(message);
    }

    public InvalidStepResultException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
