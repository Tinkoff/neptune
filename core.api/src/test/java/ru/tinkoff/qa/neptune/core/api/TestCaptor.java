package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class TestCaptor extends Captor<Number, String> {

    static final List<String> messages = new ArrayList<>();

    public TestCaptor() {
        super(List.of((toBeInjected, message) -> {
            String msg = format("%s. Result: %s", message, toBeInjected);
            messages.add(msg);
        }));
    }

    @Override
    protected String getData(Number caught) {
        return caught.toString();
    }

    @Override
    public Number getCaptured(Object toBeCaptured) {
        if (Number.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (Number) toBeCaptured;
        }

        return null;
    }
}
