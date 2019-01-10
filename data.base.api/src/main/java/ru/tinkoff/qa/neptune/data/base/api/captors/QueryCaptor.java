package ru.tinkoff.qa.neptune.data.base.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.data.base.api.GotByQuery;

import javax.jdo.Query;

import static java.util.Optional.ofNullable;

public class QueryCaptor extends StringCaptor<Query<?>> {

    public QueryCaptor() {
        super("Query");
    }

    @Override
    public StringBuilder getData(Query<?> caught) {
        return new StringBuilder().append(caught.toString());
    }

    @Override
    public Query<?> getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();
        if (Query.class.isAssignableFrom(clazz)) {
            return (Query<?>) toBeCaptured;
        }

        if (GotByQuery.class.isAssignableFrom(clazz)) {
            return ofNullable(((GotByQuery) toBeCaptured).getQuery())
                    .map(o -> {
                        if (Query.class.isAssignableFrom(o.getClass())) {
                            return (Query) o;
                        }
                        return null;
                    })
                    .orElse(null);
        }
        return null;
    }
}
