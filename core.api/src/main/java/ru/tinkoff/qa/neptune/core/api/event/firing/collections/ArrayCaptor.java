package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.IsLoggableUtil.hasReadableDescription;
import static ru.tinkoff.qa.neptune.core.api.IsLoggableUtil.isLoggable;

public class ArrayCaptor extends IterableCaptor<List<?>> {

    public ArrayCaptor() {
        super("Resulted array");
    }

    @Override
    public List<?> getCaptured(Object toBeCaptured) {

        if (!toBeCaptured.getClass().isArray()) {
            return null;
        }

        var result = stream(((Object[]) toBeCaptured))
                .filter(o -> {
                    var clazz = o.getClass();
                    return isLoggable(o)
                            || hasReadableDescription(o)
                            || clazz.isArray()
                            || Iterable.class.isAssignableFrom(clazz)
                            || Map.class.isAssignableFrom(clazz);
                })
                .collect(toList());

        if (result.size() == 0) {
            return null;
        }

        return result;
    }
}
