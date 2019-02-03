package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class TestEventLogger implements EventLogger {

    public static final List<String> MESSAGES = new ArrayList<>();

    @Override
    public void fireTheEventStarting(String message) {
        String msg = format("%s has started", message);
        MESSAGES.add(msg);
    }

    @Override
    public void fireThrownException(Throwable throwable) {
        String msg = format("%s has been thrown", throwable.getClass().getName());
        MESSAGES.add(msg);
    }

    @Override
    public void fireReturnedValue(Object returned) {
        String msg = format("%s has been returned", returned);
        MESSAGES.add(msg);
    }

    @Override
    public void fireEventFinishing() {
        MESSAGES.add("Event finished");
    }
}
