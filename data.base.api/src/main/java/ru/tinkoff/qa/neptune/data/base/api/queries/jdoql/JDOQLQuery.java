package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.KeepResultPersistent;
import ru.tinkoff.qa.neptune.data.base.api.queries.Query;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.PersistableExpression;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.ConnectionDataReader.getConnection;

/**
 * This class is designed to perform a query to select list of stored objects by {@link JDOQLTypedQuery}
 *
 * @param <T> is a type of {@link PersistableObject} to be selected
 */
public final class JDOQLQuery<T extends PersistableObject, Q extends PersistableExpression<T>> implements Query<T, List<T>>, IdSetter {

    private final Class<T> tClass;
    private final JDOQLQueryParameters<T, Q> parameters;
    private final KeepResultPersistent keepResultPersistent;

    private JDOQLQuery(Class<T> tClass, JDOQLQueryParameters<T, Q> parameters, KeepResultPersistent keepResultPersistent) {
        this.tClass = tClass;
        this.parameters = parameters;
        this.keepResultPersistent = keepResultPersistent;
    }

    /**
     * Creates an instance that performs a query to select list of stored objects by {@link JDOQLTypedQuery}
     *
     * @param <T> is a type of {@link PersistableObject} to be selected
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQuery}
     */
    public static <T extends PersistableObject, Q extends PersistableExpression<T>> JDOQLQuery<T, Q> byJDOQLQuery(
            Class<T> tClass,
            JDOQLQueryParameters<T, Q> parameters,
            KeepResultPersistent keepResultPersistent) {
        return new JDOQLQuery<>(tClass, parameters, keepResultPersistent);
    }

    @Override
    public List<T> execute(JDOPersistenceManager jdoPersistenceManager) {
        var query = ofNullable(parameters)
                .map(p -> p.buildQuery(new ReadableJDOQuery<>(jdoPersistenceManager, tClass)))
                .orElseGet(() -> new ReadableJDOQuery<>(jdoPersistenceManager, tClass));
        var list = query.executeList();

        List<T> toReturn;
        if (!keepResultPersistent.toKeepOnPersistent()) {
            toReturn = new ArrayList<>(jdoPersistenceManager.detachCopyAll(list)) {
            };
            setRealIds(list, toReturn);
        } else {
            toReturn = new ArrayList<>(list) {
            };
            keepResultPersistent.setToKeepOnPersistent(false);
        }

        return toReturn;
    }

    public String toString() {
        JDOPersistenceManager manager;
        try {
            manager = getConnection(tClass).getPersistenceManager();
        } catch (Exception e) {
            return "<Impossible to build a query>";
        }

        try {
            var query = ofNullable(parameters)
                    .map(p -> p.buildQuery(new ReadableJDOQuery<>(manager, tClass)))
                    .orElseGet(() -> new ReadableJDOQuery<>(manager, tClass));
            return "JDO typed query " + query.getInternalQuery().toString();
        } catch (Exception e) {
            return "<Impossible to build a query>";
        } finally {
            manager.close();
        }
    }
}
