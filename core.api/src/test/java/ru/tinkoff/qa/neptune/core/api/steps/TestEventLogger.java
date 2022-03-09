package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

public class TestEventLogger implements EventLogger {

    public static final List<String> MESSAGES = new ArrayList<>();
    public static final LinkedList<Map<String, String>> PARAMETERS = new LinkedList<>();
    public static final LinkedList<Map<String, String>> ADDITIONAL_PARAMETERS = new LinkedList<>();

    @Override
    public void fireTheEventStarting(String message, Map<String, String> params) {
        String msg = format("%s has started", message);

        if (nonNull(params)) {
            PARAMETERS.addLast(params);
        }
        MESSAGES.add(msg);
    }

    @Override
    public void fireThrownException(Throwable throwable) {
        String msg = format("%s has been thrown", throwable.getClass().getName());
        MESSAGES.add(msg);
    }

    @Override
    public void fireReturnedValue(String resultDescription, Object returned) {
        String msg = format("%s: %s has been returned", resultDescription, returned);
        MESSAGES.add(msg);
    }

    @Override
    public void fireEventFinishing() {
        MESSAGES.add("Event finished");
    }

    @Override
    public void addParameters(Map<String, String> parameters) {
        if (nonNull(parameters)) {
            ADDITIONAL_PARAMETERS.addLast(parameters);
        }
    }
}
