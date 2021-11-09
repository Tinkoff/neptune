package ru.tinkoff.qa.neptune.data.base.api.captors;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Deprecated(forRemoval = true)
@Description("Persistable DB objects")
public class DBObjectsCaptor extends DBCaptor<List<PersistableObject>> {

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
