package ru.tinkoff.qa.neptune.retrofit2.tests.capturing;

import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

import java.util.Map;

public class TestEventLogger implements EventLogger {

    static final ThreadLocal<Map<String, String>> ADDITIONAL_ARGUMENTS = new ThreadLocal<>();

    @Override
    public void fireTheEventStarting(String message, Map<String, String> parameters) {
    }

    @Override
    public void fireThrownException(Throwable throwable) {

    }

    @Override
    public void fireReturnedValue(String resultDescription, Object returned) {

    }

    @Override
    public void fireEventFinishing() {

    }

    @Override
    public void addParameters(Map<String, String> parameters) {
        ADDITIONAL_ARGUMENTS.set(parameters);
    }
}
