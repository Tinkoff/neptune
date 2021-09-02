package ru.tinkoff.qa.neptune.retrofit2.tests.capturing;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class TestFileInjector implements CapturedFileInjector {

    static final ThreadLocal<Map<String, File>> MESSAGES = new ThreadLocal<>();

    @Override
    public void inject(File toBeInjected, String message) {
        var map = ofNullable(MESSAGES.get())
                .orElseGet(() -> {
                    var newMap = new HashMap<String, File>();
                    MESSAGES.set(newMap);
                    return newMap;
                });
        map.put(message, toBeInjected);
    }
}
