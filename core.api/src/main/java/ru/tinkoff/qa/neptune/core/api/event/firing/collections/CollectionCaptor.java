package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Optional.ofNullable;

@Description("Resulted collection")
public class CollectionCaptor extends IterableCaptor<List<?>> {

    public CollectionCaptor() {
        super();
    }

    public CollectionCaptor(String description) {
        super(description);
    }

    @Override
    public List<?> getCaptured(Object toBeCaptured) {
        return ofNullable(toBeCaptured)
                .map(capture -> {
                    if (!Collection.class.isAssignableFrom(capture.getClass())) {
                        return null;
                    }

                    var result = new ArrayList<>(((Collection<?>) capture));
                    if (result.isEmpty()) {
                        return null;
                    }

                    return result;
                })
                .orElse(null);
    }
}
