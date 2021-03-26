package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Description("Number")
public class TestNumberCaptor extends Captor<Number, Number> {

    static final List<Number> numbers = new ArrayList<>();
    static final List<String> messages = new ArrayList<>();

    public TestNumberCaptor() {
        super(List.of((toBeInjected, message) -> {
            var msg = format("%s %s", message, toBeInjected);
            System.out.println(msg);
            messages.add(msg);
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
