package ru.tinkoff.qa.neptune.data.base.api.captors;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import static java.lang.String.format;

public class DBObjectCaptor extends DBCaptor<PersistableObject> {

    public void capture(PersistableObject caught, String message) {
        super.capture(caught, format("Received object from the DB. Step: '%s'", message));
    }

    @Override
    public PersistableObject getCaptured(Object toBeCaptured) {
        if (PersistableObject.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (PersistableObject) toBeCaptured;
        }
        return null;
    }
}
