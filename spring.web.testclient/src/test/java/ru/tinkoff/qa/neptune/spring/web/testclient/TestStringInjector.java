package ru.tinkoff.qa.neptune.spring.web.testclient;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class TestStringInjector implements CapturedStringInjector {

    private final static ThreadLocal<Map<String, String>> MESSAGES = new ThreadLocal<>();

    static Map<String, String> getMessages() {
        var map = MESSAGES.get();
        if (isNull(map)) {
            map = new LinkedHashMap<>();
            MESSAGES.set(map);
        }

        return map;
    }

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        getMessages().put(message, toBeInjected.toString());
    }
}
