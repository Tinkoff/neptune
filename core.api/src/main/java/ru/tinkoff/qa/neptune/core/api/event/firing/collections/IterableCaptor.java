package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.util.Arrays;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

abstract class IterableCaptor<T extends Iterable<?>> extends StringCaptor<T> {

    IterableCaptor(String message) {
        super(message);
    }

    @Override
    public StringBuilder getData(T caught) {
        var captured = new StringBuilder();
        caught.forEach(o -> ofNullable(o).ifPresentOrElse(o1 -> {
                    var clazz = o1.getClass();

                    if (Iterable.class.isAssignableFrom(clazz)) {
                        captured.append(format("%s\n", Iterables.toString((Iterable<?>) o1)));
                        return;
                    }

                    if (clazz.isArray()) {
                        captured.append(format("%s\n", Arrays.toString((Object[]) o1)));
                        return;
                    }

                    captured.append(format("%s\n", o1));
                },
                () -> captured.append(format("%s\n", String.valueOf(o)))));
        return captured;
    }
}
