package ru.tinkoff.qa.neptune.data.base.api.captors;

import javax.jdo.Query;

public class QueryCaptor extends AbstractQueryCaptor<Query<?>> {

    @Override
    public Query<?> getCaptured(Object toBeCaptured) {
        if (Query.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (Query<?>) toBeCaptured;
        }
        return tryToExtractQuery(toBeCaptured, Query.class);
    }
}
