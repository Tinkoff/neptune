package ru.tinkoff.qa.neptune.spring.web.testclient;

import java.util.function.Function;

final class FromBodyGet<T, R> implements Function<WebTestClientContext, T> {

    private final SendRequestAction<R> send;
    private final Function<R, T> converter;

    FromBodyGet(SendRequestAction<R> send, Function<R, T> converter) {
        this.send = send;
        this.converter = converter;
    }

    @Override
    public T apply(WebTestClientContext webTestClientContext) {
        send.get().performAction(webTestClientContext);
        return converter.apply(send.getBody());
    }
}
