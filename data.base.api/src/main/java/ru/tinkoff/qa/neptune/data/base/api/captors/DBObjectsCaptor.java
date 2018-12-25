package ru.tinkoff.qa.neptune.data.base.api.captors;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class DBObjectsCaptor extends DBCaptor<List<PersistableObject>> {

    public void capture(List<PersistableObject> caught, String message) {
        super.capture(caught, format("Received objects from the DB. Step: '%s'", message));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PersistableObject> getCaptured(Object toBeCaptured) {
        if (!List.class.isAssignableFrom(toBeCaptured.getClass())) {
            return null;
        }

        List<PersistableObject> result = ((List<Object>) toBeCaptured).stream()
                .filter(o -> nonNull(o) && PersistableObject.class.isAssignableFrom(o.getClass()))
                .map(o -> (PersistableObject) o)
                .collect(toList());

        if (result.size() > 0) {
            return result;
        }

        return null;
    }
}
