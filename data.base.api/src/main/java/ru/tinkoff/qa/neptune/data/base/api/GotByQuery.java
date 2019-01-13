package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.query.AbstractJDOQLTypedQuery;

public interface GotByQuery {

    /**
     * Returns a query that was used was used to get an object from a dada store.
     * {@link javax.jdo.Query} and {@link AbstractJDOQLTypedQuery} are used generally.
     *
     * @return the query object that was used to get an object from a dada store
     */
    Object getQuery();
}
