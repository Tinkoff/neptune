package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.HibernateFunction;

import static com.google.common.base.Preconditions.checkNotNull;


@Description("By query string")
public abstract class SelectionByQuery<R, RESULT> extends HibernateFunction<R, RESULT> {

    final String queryString;
    Object[] parameters;

    protected SelectionByQuery(Class<R> entity, String queryString, Object... parameters) {
        super(entity);
        checkNotNull(queryString);
        this.queryString = queryString;
        if (parameters.length != 0) {
            this.parameters = parameters;
        }
    }

    public static <R> SelectASingleByQuery<R> getSingleByQuery(Class<R> entity, String queryString, Object... parameters) {
        return new SelectASingleByQuery<>(entity, queryString, parameters);
    }

    public static <R> SelectIterableByQuery<R> getIterableByQuery(Class<R> entity, String queryString, Object... parameters) {
        return new SelectIterableByQuery<>(entity, queryString, parameters);
    }

    protected Query<R> formQuery(Session session) {
        var query = session.createQuery(queryString, entity);

        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                query.setParameter(i, parameters[i]);
            }
        }

        return query;
    }

    private static final class SelectASingleByQuery<R> extends SelectionByQuery<R, R> {

        private SelectASingleByQuery(Class<R> entity, String queryString, Object... parameters) {
            super(entity, queryString, parameters);
        }

        @Override
        public R apply(HibernateContext context) {
            var sessionFactory = context.getSessionFactoryByEntity(entity);
            var session = sessionFactory.getCurrentSession();

            session.beginTransaction();

            var query = formQuery(session);
            var result = query.getSingleResult();

            session.getTransaction().commit();

            return result;
        }
    }

    private static final class SelectIterableByQuery<R> extends SelectionByQuery<R, Iterable<R>> {

        private SelectIterableByQuery(Class<R> entity, String queryString, Object... parameters) {
            super(entity, queryString, parameters);
        }

        @Override
        public Iterable<R> apply(HibernateContext context) {
            var sessionFactory = context.getSessionFactoryByEntity(entity);
            var session = sessionFactory.getCurrentSession();

            session.beginTransaction();

            var query = formQuery(session);
            var result = query.getResultList();

            session.getTransaction().commit();

            return result;
        }
    }
}
