package ru.tinkoff.qa.neptune.kafka.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;

import java.util.HashMap;
import java.util.Map;

public class TestStringInjector implements CapturedStringInjector {

    static final Map<String, String> CAUGHT_MESSAGES = new HashMap<>();

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        CAUGHT_MESSAGES.put(message, toBeInjected.toString());
    }
}
