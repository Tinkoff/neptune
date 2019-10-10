package ru.tinkoff.qa.neptune.data.base.api.queries;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import org.datanucleus.api.jdo.query.JDOQLTypedQueryImpl;
import org.datanucleus.store.query.Query;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.PersistenceManager;
import java.util.function.Function;

public class ByJDOQLTypedQuery<T> implements Function<JDOPersistenceManager, T> {

    private

    @Override
    public T apply(JDOPersistenceManager jdoPersistenceManager) {
        return null;
    }

    private static class ReadableJDOQLTypedQuery<T extends PersistableObject> extends JDOQLTypedQueryImpl<T> {

        /**
         * Constructor for a typesafe query.
         *
         * @param pm             Persistence Manager
         * @param candidateClass The candidate class
         */
        public ReadableJDOQLTypedQuery(PersistenceManager pm, Class<T> candidateClass) {
            super(pm, candidateClass);
        }

        protected Query getInternalQuery() {
            return super.getInternalQuery();
        }
    }
}
