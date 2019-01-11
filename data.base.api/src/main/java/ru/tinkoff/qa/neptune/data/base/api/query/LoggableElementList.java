package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.core.api.LoggableObject;
import ru.tinkoff.qa.neptune.data.base.api.GotByQuery;
import ru.tinkoff.qa.neptune.data.base.api.captors.IsQueryCaptured;

import java.util.ArrayList;
import java.util.Collection;

class LoggableElementList<T> extends ArrayList<T> implements LoggableObject, GotByQuery, IsQueryCaptured {

    private transient Object query;
    private boolean isCaptured;

    LoggableElementList() {
        super();
    }

    LoggableElementList(Collection<T> toAdd) {
        super(toAdd);
    }

    @Override
    public Object getQuery() {
        return query;
    }

    LoggableElementList<T> setQuery(Object query) {
        this.query = query;
        return this;
    }

    @Override
    public boolean isCaptured() {
        return isCaptured;
    }

    @Override
    public void setCaptured() {
        isCaptured = true;
    }
}
