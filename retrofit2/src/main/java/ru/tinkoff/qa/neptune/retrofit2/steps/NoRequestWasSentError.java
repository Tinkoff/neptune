package ru.tinkoff.qa.neptune.retrofit2.steps;

class NoRequestWasSentError extends Error {

    NoRequestWasSentError() {
        super("No http request was sent");
    }
}
