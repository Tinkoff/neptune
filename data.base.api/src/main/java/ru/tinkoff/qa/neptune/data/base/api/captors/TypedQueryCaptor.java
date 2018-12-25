package ru.tinkoff.qa.neptune.data.base.api.captors;

import org.datanucleus.api.jdo.query.AbstractJDOQLTypedQuery;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

public class TypedQueryCaptor extends StringCaptor<AbstractJDOQLTypedQuery<?>> {

    @Override
    protected StringBuilder getData(AbstractJDOQLTypedQuery<?> caught) {
        var result = new StringBuilder("Query: \n");
        result.append(caught.toString());
        return result;
    }

    @Override
    public AbstractJDOQLTypedQuery<?> getCaptured(Object toBeCaptured) {
        if (AbstractJDOQLTypedQuery.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (AbstractJDOQLTypedQuery<?>) toBeCaptured;
        }

        return null;
    }
}
