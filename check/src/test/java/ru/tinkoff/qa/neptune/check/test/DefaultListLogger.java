package ru.tinkoff.qa.neptune.check.test;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.ArrayList;
import java.util.List;

public class DefaultListLogger extends Captor<Object, String> {

    static final List<String> messages = new ArrayList<>();

    public DefaultListLogger() {
        super(List.of((toBeInjected, message) -> {
            System.out.println(message);
            messages.add(message);
        }));
    }

    @Override
    protected String getData(Object caught) {
        return caught.toString();
    }

    @Override
    public Class<Object> getTypeToBeCaptured() {
        return Object.class;
    }
}
