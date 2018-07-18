package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class TestCapturedFileInjector implements CapturedFileInjector {

    static final List<String> messages = new ArrayList<>();

    @Override
    public void inject(File toBeInjected, String message) {
        String msg = format("%s. Result: %s", message, toBeInjected.getName());
        messages.add(msg);
    }
}
