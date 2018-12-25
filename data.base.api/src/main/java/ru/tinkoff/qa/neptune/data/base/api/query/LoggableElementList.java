package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.core.api.LoggableObject;

import java.util.ArrayList;
import java.util.Collection;

class LoggableElementList<T> extends ArrayList<T> implements LoggableObject {

    LoggableElementList() {
        super();
    }

    LoggableElementList(Collection<T> toAdd) {
        super(toAdd);
    }
}
