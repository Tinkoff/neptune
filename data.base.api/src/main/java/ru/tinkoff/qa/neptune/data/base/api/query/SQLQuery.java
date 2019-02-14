package ru.tinkoff.qa.neptune.data.base.api.query;

import org.datanucleus.api.jdo.JDOQuery;

public final class SQLQuery<T> {
    private final JDOQuery<T> query;
    private final String stringQuery;

    SQLQuery(JDOQuery<T> query, String stringQuery) {
        this.query = query;
        this.stringQuery = stringQuery;
    }

    JDOQuery<T> getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return stringQuery;
    }
}
