package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;
import static ru.tinkoff.qa.neptune.core.api.utils.ToArrayUtil.toArray;

@Description("Resulted map")
public class MapCaptor extends StringCaptor<Map<?, ?>> {

    private static final String LINE_SEPARATOR = "\r\n";

    private String stringValueOf(Object o) {
        return ofNullable(o)
                .map(o1 -> {
                    var clazz = o1.getClass();

                    if (Iterable.class.isAssignableFrom(clazz)) {
                        return Iterables.toString((Iterable<?>) o1);
                    }

                    if (clazz.isArray()) {
                        return Arrays.toString(toArray(o1));
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
        return ofNullable(toBeCaptured)
                .map(capture -> {
                    if (!Map.class.isAssignableFrom(capture.getClass())) {
                        return null;
                    }

                    var list = ((Map<?, ?>) capture).entrySet().stream().filter(entry -> {
                        var key = entry.getKey();
                        var value = entry.getValue();
                        Class<?> keyClazz;
                        Class<?> valueClazz;
                        return (isLoggable(key)
                                || (isLoggable(value))
                                || (keyClazz = key.getClass()).isArray()
                                || Iterable.class.isAssignableFrom(keyClazz)
                                || Map.class.isAssignableFrom(keyClazz)
                                || (valueClazz = value.getClass()).isArray()
                                || Iterable.class.isAssignableFrom(valueClazz)
                                || Map.class.isAssignableFrom(valueClazz));
                    }).collect(Collectors.toCollection(LinkedList::new));

                    Map<Object, Object> result = new LinkedHashMap<>();
                    list.forEach(e -> result.put(e.getKey(), e.getValue()));

                    if (result.isEmpty()) {
                        return null;
                    }

                    return result;
                })
                .orElse(null);
    }
}
