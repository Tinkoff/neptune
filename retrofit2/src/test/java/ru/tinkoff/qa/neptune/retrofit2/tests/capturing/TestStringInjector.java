package ru.tinkoff.qa.neptune.retrofit2.tests.capturing;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class TestStringInjector implements CapturedStringInjector {

    static final ThreadLocal<Map<String, StringBuilder>> MESSAGES = new ThreadLocal<>();

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        var map = ofNullable(MESSAGES.get())
                .orElseGet(() -> {
                    var newMap = new HashMap<String, StringBuilder>();
                    MESSAGES.set(newMap);
                    return newMap;
                });
        map.put(message, toBeInjected);
    }
}
