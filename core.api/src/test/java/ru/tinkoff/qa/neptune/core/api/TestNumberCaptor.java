package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class TestNumberCaptor extends Captor<Number, Number> {

    static final List<Number> numbers = new ArrayList<>();

    public TestNumberCaptor() {
        super("Number", List.of((toBeInjected, message) -> {
            System.out.println(format("%s %s", message, toBeInjected));
            numbers.add(toBeInjected);
        }));
    }

    @Override
    public Number getData(Number caught) {
        return caught;
    }

    @Override
    public Number getCaptured(Object toBeCaptured) {
        if (Number.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (Number) toBeCaptured;
        }

        return null;
    }
}
