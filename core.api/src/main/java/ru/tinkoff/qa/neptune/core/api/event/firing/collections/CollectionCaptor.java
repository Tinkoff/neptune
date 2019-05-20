package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.hasReadableDescription;

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
                .filter(o -> {
                    var clazz = ofNullable(o)
                            .map(Object::getClass)
                            .orElse(null);

                    return isLoggable(o)
                            || hasReadableDescription(o)
                            || ofNullable(clazz).map(aClass -> aClass.isArray()
                            || Iterable.class.isAssignableFrom(aClass)
                            || Map.class.isAssignableFrom(aClass)).orElse(false);
                }).collect(toList());

        if (result.size() == 0) {
            return null;
        }

        return result;
    }
}
