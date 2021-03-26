package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.util.Arrays;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

abstract class IterableCaptor<T extends Iterable<?>> extends StringCaptor<T> {

    private static final String LINE_SEPARATOR = "\r\n";

    @Override
    public StringBuilder getData(T caught) {
        var captured = new StringBuilder();
        caught.forEach(o -> ofNullable(o).ifPresentOrElse(o1 -> {
                    var clazz = o1.getClass();

                    if (Iterable.class.isAssignableFrom(clazz)) {
                        captured.append(format("%s%s", Iterables.toString((Iterable<?>) o1), LINE_SEPARATOR));
                        return;
                    }

                    if (clazz.isArray()) {
                        captured.append(format("%s%s", Arrays.toString((Object[]) o1), LINE_SEPARATOR));
                        return;
                    }

                    captured.append(format("%s%s", o1, LINE_SEPARATOR));
                },
                () -> captured.append(format("%s%s", o, LINE_SEPARATOR))));
        return captured;
    }
}
