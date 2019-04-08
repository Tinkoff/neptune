package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.util.Arrays;
import java.util.Map;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.System.lineSeparator;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.hasReadableDescription;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

public class MapCaptor extends StringCaptor<Map<?, ?>> {

    private static final String LINE_SEPARATOR = lineSeparator();

    public MapCaptor() {
        super("Resulted map");
    }

    private String stringValueOf(Object o) {
        return ofNullable(o)
                .map(o1 -> {
                    var clazz = o1.getClass();

                    if (Iterable.class.isAssignableFrom(clazz)) {
                        return format("%s%s", Iterables.toString((Iterable<?>) o1), LINE_SEPARATOR);
                    }

                    if (clazz.isArray()) {
                        return format("%s%s", Arrays.toString((Object[]) o1), LINE_SEPARATOR);
                    }

                    return o1.toString();
                }).orElseGet(() -> valueOf(o));
    }

    @Override
    public StringBuilder getData(Map<?, ?> caught) {
        var captured = new StringBuilder();
        caught.forEach((key, value) -> {
            String stringKey = stringValueOf(key);
            String stringValue = stringValueOf(value);
            captured.append(format("Key = %s, Value = %s%s", stringKey, stringValue, LINE_SEPARATOR));
        });
        return captured;
    }

    @Override
    public Map<?, ?> getCaptured(Object toBeCaptured) {
        if (!Map.class.isAssignableFrom(toBeCaptured.getClass())) {
            return null;
        }

        var result = ((Map<?, ?>) toBeCaptured).entrySet().stream().filter(entry -> {
            var key = entry.getKey();
            var value = entry.getValue();
            Class<?> keyClazz;
            Class<?> valueClazz;
            return (isLoggable(key)
                    || hasReadableDescription(key))
                    || (isLoggable(value)
                    || hasReadableDescription(value)
                    || (keyClazz = key.getClass()).isArray()
                    || Iterable.class.isAssignableFrom(keyClazz)
                    || Map.class.isAssignableFrom(keyClazz)
                    || (valueClazz = value.getClass()).isArray()
                    || Iterable.class.isAssignableFrom(valueClazz)
                    || Map.class.isAssignableFrom(valueClazz));
        }).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (result.size() == 0) {
            return null;
        }

        return result;
    }
}
