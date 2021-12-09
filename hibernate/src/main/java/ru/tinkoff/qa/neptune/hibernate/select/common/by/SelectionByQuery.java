package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;


@SuppressWarnings("unchecked")
@Description("By query string")
public abstract class SelectionByQuery<RESULT> implements Function<Class<?>, RESULT> {

    final String queryString;
    Object[] parameters;

    public SelectionByQuery(String queryString, Object... parameters) {
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

    public static <R> SelectASingleByQuery<R> getSingleByQuery(String queryString, Object... parameters) {
        return new SelectASingleByQuery<>(queryString, parameters);
    }

    public static <R> SelectIterableByQuery<R> getIterableByQuery(String queryString, Object... parameters) {
        return new SelectIterableByQuery<>(queryString, parameters);
    }

    private static final class SelectASingleByQuery<R> extends SelectionByQuery<R> {

        private SelectASingleByQuery(String queryString, Object... parameters) {
            super(queryString, parameters);
        }

        @Override
        public R apply(Class<?> t) {
            var sessionFactory = getSessionFactoryByEntity(t);
            var session = sessionFactory.getCurrentSession();
            var query = session.createQuery(queryString, t);

            for (int i = 0; i < parameters.length; i++) {
                query.setParameter(i, parameters[i]);
            }

            return (R) query.getSingleResult();
        }
    }

    private static final class SelectIterableByQuery<R> extends SelectionByQuery<Iterable<R>> {

        private SelectIterableByQuery(String queryString, Object... parameters) {
            super(queryString, parameters);
        }

        @Override
        public Iterable<R> apply(Class<?> t) {
            var sessionFactory = getSessionFactoryByEntity(t);
            var session = sessionFactory.getCurrentSession();
            var query = session.createQuery(queryString, t);

            for (int i = 0; i < parameters.length; i++) {
                query.setParameter(i, parameters[i]);
            }

            return (List<R>) query.getResultList();
        }
    }
}
