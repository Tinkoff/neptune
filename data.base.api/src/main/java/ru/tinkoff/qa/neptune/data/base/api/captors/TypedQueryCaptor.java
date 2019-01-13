package ru.tinkoff.qa.neptune.data.base.api.captors;

import org.datanucleus.api.jdo.query.AbstractJDOQLTypedQuery;

public class TypedQueryCaptor extends AbstractQueryCaptor<AbstractJDOQLTypedQuery<?>> {

    @Override
    public AbstractJDOQLTypedQuery<?> getCaptured(Object toBeCaptured) {
        if (AbstractJDOQLTypedQuery.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (AbstractJDOQLTypedQuery<?>) toBeCaptured;
        }
        return tryToExtractQuery(toBeCaptured, AbstractJDOQLTypedQuery.class);
    }
}
