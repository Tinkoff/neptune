package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.IsLoggableUtil.hasReadableDescription;
import static ru.tinkoff.qa.neptune.core.api.IsLoggableUtil.isLoggable;

public class CollectionCaptor extends IterableCaptor<List<?>> {

    public CollectionCaptor() {
        super("Resulted collection");
    }

    @Override
    public List<?> getCaptured(Object toBeCaptured) {
        if (!Collection.class.isAssignableFrom(toBeCaptured.getClass())) {
            return null;
        }

        var result = ((Collection<?>) toBeCaptured)
                .stream()
                .filter(o -> isLoggable(o) || hasReadableDescription(o)).collect(toList());

        if (result.size() == 0) {
            return null;
        }

        return result;
    }
}
