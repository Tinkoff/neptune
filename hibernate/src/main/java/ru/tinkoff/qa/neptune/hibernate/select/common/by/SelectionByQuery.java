package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import org.hibernate.query.Query;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.HibernateFunction;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;


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

    @Override
    public String toString() {
        return translate(this);
    }

    public static <R> SelectASingleByQuery<R> getSingleByQuery(Class<R> entity, String queryString, Object... parameters) {
        return new SelectASingleByQuery<>(entity, queryString, parameters);
    }

    public static <R> SelectIterableByQuery<R> getIterableByQuery(Class<R> entity, String queryString, Object... parameters) {
        return new SelectIterableByQuery<>(entity, queryString, parameters);
    }

    protected Query<R> formQuery(HibernateContext context) {
        var sessionFactory = context.getSessionFactoryByEntity(entity);
        var session = sessionFactory.getCurrentSession();
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
            var query = formQuery(context);

            return query.getSingleResult();
        }
    }

    private static final class SelectIterableByQuery<R> extends SelectionByQuery<R, Iterable<R>> {

        private SelectIterableByQuery(Class<R> entity, String queryString, Object... parameters) {
            super(entity, queryString, parameters);
        }

        @Override
        public Iterable<R> apply(HibernateContext context) {
            var query = formQuery(context);

            return query.getResultList();
        }
    }
}
