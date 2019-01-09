package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.util.Map;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;
import static ru.tinkoff.qa.neptune.core.api.IsLoggableUtil.hasReadableDescription;
import static ru.tinkoff.qa.neptune.core.api.IsLoggableUtil.isLoggable;

public class MapCaptor extends StringCaptor<Map<?, ?>> {

    public MapCaptor() {
        super("Resulted map");
    }

    @Override
    public StringBuilder getData(Map<?, ?> caught) {
        var captured = new StringBuilder();
        caught.forEach((key, value) -> captured.append(format("Key = %s, Value = %s\n", String.valueOf(key), String.valueOf(value))));
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
            return (isLoggable(key) || hasReadableDescription(key)) || (isLoggable(value) || hasReadableDescription(value));
        }).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (result.size() == 0) {
            return null;
        }

        return result;
    }
}
