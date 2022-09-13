package ru.tinkoff.qa.neptune.core.api.event.firing.collections;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.utils.ToArrayUtil.toArray;

@Description("Resulted array")
public class ArrayCaptor extends CollectionCaptor {

    public ArrayCaptor() {
        super();
    }

    public ArrayCaptor(String description) {
        super(description);
    }

    @Override
    public List<?> getCaptured(Object toBeCaptured) {

        return ofNullable(toBeCaptured).map(capture -> {
            if (!capture.getClass().isArray()) {
                return null;
            }

            return super.getCaptured(asList(toArray(capture)));
        }).orElse(null);
    }
}
