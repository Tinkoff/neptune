package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.GotByQuery;
import ru.tinkoff.qa.neptune.data.base.api.ListOfStoredObjects;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.captors.IsQueryCaptured;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@Deprecated
class ListOfSelectObjects<T> extends ListOfStoredObjects<T> implements GotByQuery, IsQueryCaptured {

    private transient Object query;
    private boolean isCaptured;

    ListOfSelectObjects(Collection<T> toAdd, Function<List<T>, String> functionToCreateDescription) {
        super("List of selected objects", toAdd, functionToCreateDescription);
    }

    ListOfSelectObjects(String description, Collection<T> toAdd) {
        super(description, toAdd);
    }

    @Override
    public Object getQuery() {
        return query;
    }

    ListOfSelectObjects<T> setQuery(Object query) {
        this.query = query;

        if (!String.class.isAssignableFrom(query.getClass())) {
            forEach(t -> ofNullable(t).ifPresent(t1 -> {
                if (PersistableObject.class.isAssignableFrom(t1.getClass())) {
                    ((PersistableObject) t1).setQuery(query);
                }
            }));
        }
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
