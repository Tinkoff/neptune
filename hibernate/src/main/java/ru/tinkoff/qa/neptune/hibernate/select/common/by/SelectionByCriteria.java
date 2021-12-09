package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import javax.persistence.criteria.CriteriaQuery;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;

@Description("by JPA criteria")
public abstract class SelectionByCriteria<R, RESULT> implements Function<Class<R>, RESULT> {

    final CriteriaQuery<R> criteriaQuery;

    public SelectionByCriteria(CriteriaQuery<R> criteriaQuery) {
        checkNotNull(criteriaQuery);
        this.criteriaQuery = criteriaQuery;
    }

    @Override
    public String toString() {
        return translate(this);
    }

    public static <R> SelectASingleByCriteria<R> getSingleByCriteria(CriteriaQuery<R> criteriaQuery) {
        return new SelectASingleByCriteria<>(criteriaQuery);
    }

    public static <R> SelectIterableByCriteria<R> getIterableByCriteria(CriteriaQuery<R> criteriaQuery) {
        return new SelectIterableByCriteria<>(criteriaQuery);
    }

    private static final class SelectASingleByCriteria<R> extends SelectionByCriteria<R, R> {

        private SelectASingleByCriteria(CriteriaQuery<R> criteriaQuery) {
            super(criteriaQuery);
        }

        @Override
        public R apply(Class<R> t) {
            var sessionFactory = getSessionFactoryByEntity(t);
            var session = sessionFactory.getCurrentSession();

            return session.createQuery(criteriaQuery).getSingleResult();
        }
    }

    private static final class SelectIterableByCriteria<R> extends SelectionByCriteria<R, Iterable<R>> {

        private SelectIterableByCriteria(CriteriaQuery<R> criteriaQuery) {
            super(criteriaQuery);
        }

        @Override
        public Iterable<R> apply(Class<R> t) {
            var sessionFactory = getSessionFactoryByEntity(t);
            var session = sessionFactory.getCurrentSession();

            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}
