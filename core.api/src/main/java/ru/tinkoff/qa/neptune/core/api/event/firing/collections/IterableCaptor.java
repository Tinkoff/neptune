package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import static java.lang.String.format;

abstract class IterableCaptor<T extends Iterable<?>> extends StringCaptor<T> {

    IterableCaptor(String message) {
        super(message);
    }

    @Override
    public StringBuilder getData(T caught) {
        var captured = new StringBuilder();
        caught.forEach(o -> captured.append(format("%s\n", String.valueOf(o))));
        return captured;
    }
}
