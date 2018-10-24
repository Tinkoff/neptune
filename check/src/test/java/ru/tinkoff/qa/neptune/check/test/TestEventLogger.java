package ru.tinkoff.qa.neptune.check.test;

import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class TestEventLogger implements EventLogger {

    static final List<String> MESSAGES = new ArrayList<>();

    @Override
    public void fireTheEventStarting(String message) {
        var msg = format("%s has started", message);
        MESSAGES.add(msg);
    }

    @Override
    public void fireThrownException(Throwable throwable) {
        var msg = format("%s has been thrown", throwable.getClass().getName());
        MESSAGES.add(msg);
    }

    @Override
    public void fireReturnedValue(Object returned) {
        var msg = format("%s has been returned", returned);
        MESSAGES.add(msg);
    }

    @Override
    public void fireEventFinishing() {
        MESSAGES.add("Event finished");
    }
}
