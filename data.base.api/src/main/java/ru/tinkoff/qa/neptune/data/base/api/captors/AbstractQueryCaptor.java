package ru.tinkoff.qa.neptune.data.base.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.data.base.api.GotByQuery;

import static java.util.Optional.ofNullable;

abstract class AbstractQueryCaptor<T> extends StringCaptor<T> {

    public AbstractQueryCaptor() {
        super("Query");
    }

    @Override
    public StringBuilder getData(T caught) {
        return new StringBuilder(caught.toString());
    }

    @SuppressWarnings("unchecked")
    <T> T tryToExtractQuery(Object containsQuery, Class<T> expectedClass) {
        Object query = null;
        GotByQuery gotByQuery;
        IsQueryCaptured isQueryCaptured =  null;

        var containsQueryClass = containsQuery.getClass();
        if (GotByQuery.class.isAssignableFrom(containsQueryClass)) {
            gotByQuery = (GotByQuery) containsQuery;
            query = gotByQuery.getQuery();

            if (IsQueryCaptured.class.isAssignableFrom(containsQueryClass)) {
                isQueryCaptured = (IsQueryCaptured) gotByQuery;
            }

            containsQueryClass = query.getClass();
            while (GotByQuery.class.isAssignableFrom(query.getClass())) {
                gotByQuery = (GotByQuery) query;
                query = gotByQuery.getQuery();

                if (IsQueryCaptured.class.isAssignableFrom(containsQueryClass)) {
                    isQueryCaptured = (IsQueryCaptured) gotByQuery;
                }
            }
        }

        if (query == null) {
            return null;
        }

        var queryToReturn = (T) query;

        return ofNullable(isQueryCaptured).map(isQueryCaptured1 -> {
            if (isQueryCaptured1.isCaptured()) {
                return null;
            }

            if (expectedClass.isAssignableFrom(queryToReturn.getClass())) {
                isQueryCaptured1.setCaptured();
                return queryToReturn;
            }

            return null;
        }).orElse(queryToReturn);
    }
}
