package ru.tinkoff.qa.neptune.data.base.api.captors;

import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

@Description("Persistable DB object")
public class DBObjectCaptor extends DBCaptor<PersistableObject> {

    @Override
    public PersistableObject getCaptured(Object toBeCaptured) {
        if (PersistableObject.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (PersistableObject) toBeCaptured;
        }
        return null;
    }
}
