package ru.tinkoff.qa.neptune.data.base.api.captors;

import org.datanucleus.api.jdo.query.AbstractJDOQLTypedQuery;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.data.base.api.GotByQuery;

import static java.util.Optional.ofNullable;

public class TypedQueryCaptor extends StringCaptor<AbstractJDOQLTypedQuery<?>> {

    public TypedQueryCaptor() {
        super("Typed query");
    }

    @Override
    public StringBuilder getData(AbstractJDOQLTypedQuery<?> caught) {
        return new StringBuilder().append(caught.toString());
    }

    @Override
    public AbstractJDOQLTypedQuery<?> getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();

        if (AbstractJDOQLTypedQuery.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (AbstractJDOQLTypedQuery<?>) toBeCaptured;
        }

        if (GotByQuery.class.isAssignableFrom(clazz)) {
            return ofNullable(((GotByQuery) toBeCaptured).getQuery())
                    .map(o -> {
                        if (AbstractJDOQLTypedQuery.class.isAssignableFrom(o.getClass())) {
                            return (AbstractJDOQLTypedQuery) o;
                        }
                        return null;
                    })
                    .orElse(null);
        }
        return null;
    }
}
