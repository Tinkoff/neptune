package ru.tinkoff.qa.neptune.data.base.api;

import ru.tinkoff.qa.neptune.core.api.steps.LoggableObject;

import java.util.ArrayList;
import java.util.Collection;

public class ListOfDataBaseObjects<T> extends ArrayList<T> implements LoggableObject {

    public ListOfDataBaseObjects() {
        super();
    }

    public ListOfDataBaseObjects(Collection<T> from) {
        super(from);
    }
}
