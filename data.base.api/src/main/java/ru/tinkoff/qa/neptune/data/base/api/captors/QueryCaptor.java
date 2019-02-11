package ru.tinkoff.qa.neptune.data.base.api.captors;

import ru.tinkoff.qa.neptune.data.base.api.query.SQLQuery;

public class QueryCaptor extends AbstractQueryCaptor<SQLQuery<?>> {

    @Override
    public SQLQuery<?> getCaptured(Object toBeCaptured) {
        if (SQLQuery.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (SQLQuery<?>) toBeCaptured;
        }
        return tryToExtractQuery(toBeCaptured, SQLQuery.class);
    }
}
