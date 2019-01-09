package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import java.util.List;

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
                .filter(o -> isLoggable(o) || hasReadableDescription(o)).collect(toList());

        if (result.size() == 0) {
            return null;
        }

        return result;
    }
}
