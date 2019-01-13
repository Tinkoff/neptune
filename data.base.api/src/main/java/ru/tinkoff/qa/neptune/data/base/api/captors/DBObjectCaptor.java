package ru.tinkoff.qa.neptune.data.base.api.captors;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

public class DBObjectCaptor extends DBCaptor<PersistableObject> {


    public DBObjectCaptor() {
        super("Persistable DB object");
    }

    @Override
    public PersistableObject getCaptured(Object toBeCaptured) {
        if (PersistableObject.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (PersistableObject) toBeCaptured;
        }
        return null;
    }
}
