package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.KeepResultPersistent;
import ru.tinkoff.qa.neptune.data.base.api.queries.Query;
import ru.tinkoff.qa.neptune.data.base.api.result.ListOfPersistentObjects;

import javax.jdo.JDOQLTypedQuery;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * This class is designed to perform a query to select list of stored objects by {@link JDOQLTypedQuery}
 *
 * @param <T> is a type of {@link PersistableObject} to be selected
 */
public final class JDOQLQuery<T extends PersistableObject> implements Query<T, List<T>>, IdSetter {

    private ReadableJDOQuery<T> query;
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
    public List<T> execute(JDOPersistenceManager manager) {
        var list = query.executeList();

        ListOfPersistentObjects<T> toReturn;
        if (!keepResultPersistent.toKeepOnPersistent()) {
            toReturn = new ListOfPersistentObjects<>(manager.detachCopyAll(list)) {
            };
            setRealIds(list, toReturn);
        } else {
            toReturn = new ListOfPersistentObjects<>(list) {
            };
        }


        return toReturn;
    }

    public void setQuery(ReadableJDOQuery<T> query) {
        this.query = query;
    }

    public String toString() {
        return ofNullable(query)
                .map(r -> "JDO typed query " + r.getInternalQuery().toString())
                .orElse(EMPTY);
    }
}
