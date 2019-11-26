package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.KeepResultPersistent;
import ru.tinkoff.qa.neptune.data.base.api.result.ListOfPersistentObjects;

import javax.jdo.JDOQLTypedQuery;
import java.util.List;
import java.util.function.Function;

/**
 * This class is designed to perform a query to select list of stored objects by {@link JDOQLTypedQuery}
 * @param <T> is a type of {@link PersistableObject} to be selected
 */
public final class JDOQLQuery<T extends PersistableObject> implements Function<ReadableJDOQuery<T>, List<T>>, IdSetter {

    private final KeepResultPersistent keepResultPersistent;

    private JDOQLQuery(KeepResultPersistent keepResultPersistent) {
        super();
        this.keepResultPersistent = keepResultPersistent;
    }

    /**
     * Creates an instance that performs a query to select list of stored objects by {@link JDOQLTypedQuery}
     *
     * @param <T> is a type of {@link PersistableObject} to be selected
     * @return new {@link JDOQLQuery}
     */
    public static <T extends PersistableObject> JDOQLQuery<T> byJDOQLQuery(KeepResultPersistent keepResultPersistent) {
        return new JDOQLQuery<>(keepResultPersistent);
    }

    @Override
    public List<T> apply(ReadableJDOQuery<T> jdoqlTypedQuery) {
        var list = jdoqlTypedQuery.executeList();
        var manager = jdoqlTypedQuery.getPersistenceManager();

        ListOfPersistentObjects<T> toReturn;
        if (!keepResultPersistent.toKeepOnPersistent()) {
            toReturn = new ListOfPersistentObjects<>(manager.detachCopyAll(list)) {
            };
            setRealIds(list, toReturn);
        }
        else {
            toReturn = new ListOfPersistentObjects<>(list) {
            };
        }


        return toReturn;
    }
}
