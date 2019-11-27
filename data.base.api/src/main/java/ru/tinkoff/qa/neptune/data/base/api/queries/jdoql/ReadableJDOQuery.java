package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import org.datanucleus.api.jdo.query.JDOQLTypedQueryImpl;
import org.datanucleus.store.query.Query;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.PersistenceManager;

/**
 * This class is the same is original {@link JDOQLTypedQueryImpl} except
 * ability to get native value of the query to perform
 *
 * @param <T> is a type of values to select by the query
 */
public class ReadableJDOQuery<T extends PersistableObject> extends JDOQLTypedQueryImpl<T> {
    /**
     * Constructor for a typesafe query.
     *
     * @param pm             Persistence Manager
     * @param candidateClass The candidate class
     */
    public ReadableJDOQuery(PersistenceManager pm, Class<T> candidateClass) {
        super(pm, candidateClass);
        getInternalQuery().addExtension("datanucleus.query.resultCacheType", "none");
    }

    public Query getInternalQuery() {
        return super.getInternalQuery();
    }

    public Object executeInternalQuery(Query internalQuery) {
        return super.executeInternalQuery(internalQuery);
    }
}
