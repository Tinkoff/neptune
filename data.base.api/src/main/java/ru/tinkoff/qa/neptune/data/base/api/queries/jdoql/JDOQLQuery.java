package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.result.ListOfPersistentObjects;

import javax.jdo.JDOQLTypedQuery;
import java.util.List;
import java.util.function.Function;

/**
 * This class is designed to perform a query to select list of stored objects by {@link JDOQLTypedQuery}
 * @param <T> is a type of {@link PersistableObject} to be selected
 */
public final class JDOQLQuery<T extends PersistableObject> implements Function<ReadableJDOQuery<T>, List<T>>, IdSetter {

    private JDOQLQuery() {
        super();
    }

    /**
     * Creates an instance that performs a query to select list of stored objects by {@link JDOQLTypedQuery}
     *
     * @param <T> is a type of {@link PersistableObject} to be selected
     * @return new {@link JDOQLQuery}
     */
    public static <T extends PersistableObject> JDOQLQuery<T> byJDOQLQuery() {
        return new JDOQLQuery<>();
    }

    @Override
    public List<T> apply(ReadableJDOQuery<T> jdoqlTypedQuery) {
        var list = jdoqlTypedQuery.executeList();
        var manager = jdoqlTypedQuery.getPersistenceManager();

        var toReturn =  new ListOfPersistentObjects<>(manager.detachCopyAll(list)) {
        };

        setRealIds(list, toReturn);
        return toReturn;
    }
}
