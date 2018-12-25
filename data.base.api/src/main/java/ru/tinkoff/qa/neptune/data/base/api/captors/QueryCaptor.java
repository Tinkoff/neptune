package ru.tinkoff.qa.neptune.data.base.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import javax.jdo.Query;

public class QueryCaptor extends StringCaptor<Query<?>> {

    @Override
    protected StringBuilder getData(Query<?> caught) {
        var result = new StringBuilder("Query: \n");
        result.append(caught.toString());
        return result;
    }

    @Override
    public Query<?> getCaptured(Object toBeCaptured) {
        if (Query.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (Query<?>) toBeCaptured;
        }

        return null;
    }
}
