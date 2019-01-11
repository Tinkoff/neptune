package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class TestStringInjector implements CapturedStringInjector {

    static final List<String> INJECTED = new ArrayList<>();

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        INJECTED.add(format("%s:", message) +
                toBeInjected);
    }
}
