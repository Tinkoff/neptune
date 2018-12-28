package ru.tinkoff.qa.neptune.check.test;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class DefaultListLogger extends Captor<Object, StringBuilder> {

    static final List<String> messages = new ArrayList<>();

    public DefaultListLogger() {
        super("Result is", List.of((toBeInjected, message) -> {
            var toBeLogged = format("%s %s", message, toBeInjected.toString());
            System.out.println(toBeLogged);
            messages.add(toBeLogged);
        }));
    }

    @Override
    public StringBuilder getData(Object caught) {
        return new StringBuilder(caught.toString());
    }

    @Override
    public Object getCaptured(Object toBeCaptured) {
        return toBeCaptured;
    }
}
