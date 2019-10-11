package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import java.util.List;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.data.base.api.ListOfStoredObjects.INFO_PERSISTABLE_INFO;

@Deprecated
class SelectByTypedQuery<T extends PersistableObject> implements Function<JDOQLTypedQuery<T>, List<T>> {
    @Override
    public List<T> apply(JDOQLTypedQuery<T> jdoTypedQuery) {
        return new ListOfSelectObjects<>(jdoTypedQuery.executeList(), INFO_PERSISTABLE_INFO::apply)
                        .setQuery(jdoTypedQuery);
    }
}
