package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;
import static ru.tinkoff.qa.neptune.core.api.utils.ToArrayUtil.toArray;

public class ArrayCaptor extends IterableCaptor<List<?>> {

    public ArrayCaptor() {
        this("Resulted array");
    }

    /**
     * Constructor for situations when it is necessary to override the class
     *
     * @param message is a name of attachment
     */
    protected ArrayCaptor(String message) {
        super(message);
    }

    @Override
    public List<?> getCaptured(Object toBeCaptured) {

        if (!toBeCaptured.getClass().isArray()) {
            return null;
        }

        var result = stream(toArray(toBeCaptured))
                .filter(o -> {
                    var clazz = ofNullable(o)
                            .map(Object::getClass)
                            .orElse(null);

                    return isLoggable(o)
                            || ofNullable(clazz).map(aClass -> aClass.isArray()
                            || Iterable.class.isAssignableFrom(aClass)
                            || Map.class.isAssignableFrom(aClass)).orElse(false);
                })
                .collect(toList());

        if (result.size() == 0) {
            return null;
        }

        return result;
    }
}
