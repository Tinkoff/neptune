package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class TestCapturedStringInjector implements CapturedStringInjector {

    public static final List<String> messages = new ArrayList<>();

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        String msg = format("%s %s", message, toBeInjected);
        messages.add(msg);
    }
}
