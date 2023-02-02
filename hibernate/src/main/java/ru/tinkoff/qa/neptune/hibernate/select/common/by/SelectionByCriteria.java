package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.HibernateFunction;

import javax.persistence.criteria.CriteriaQuery;

import static com.google.common.base.Preconditions.checkNotNull;

@Description("by JPA criteria")
public abstract class SelectionByCriteria<R, RESULT> extends HibernateFunction<R, RESULT> {

    final CriteriaQuery<R> criteriaQuery;

    protected SelectionByCriteria(Class<R> entity, CriteriaQuery<R> criteriaQuery) {
        super(entity);
        checkNotNull(criteriaQuery);
        this.criteriaQuery = criteriaQuery;
    }

    public static <R> SelectASingleByCriteria<R> getSingleByCriteria(Class<R> entity, CriteriaQuery<R> criteriaQuery) {
        return new SelectASingleByCriteria<>(entity, criteriaQuery);
    }

    public static <R> SelectIterableByCriteria<R> getIterableByCriteria(Class<R> entity, CriteriaQuery<R> criteriaQuery) {
        return new SelectIterableByCriteria<>(entity, criteriaQuery);
    }

    private static final class SelectASingleByCriteria<R> extends SelectionByCriteria<R, R> {

        private SelectASingleByCriteria(Class<R> entity, CriteriaQuery<R> criteriaQuery) {
            super(entity, criteriaQuery);
        }

        @Override
        public R apply(HibernateContext context) {
            var sessionFactory = context.getSessionFactoryByEntity(entity);
            var session = sessionFactory.getCurrentSession();

            session.beginTransaction();

            var result = session.createQuery(criteriaQuery).getSingleResult();

            session.getTransaction().commit();

            return result;
        }
    }

    private static final class SelectIterableByCriteria<R> extends SelectionByCriteria<R, Iterable<R>> {

        private SelectIterableByCriteria(Class<R> entity, CriteriaQuery<R> criteriaQuery) {
            super(entity, criteriaQuery);
        }

        @Override
        public Iterable<R> apply(HibernateContext context) {
            var sessionFactory = context.getSessionFactoryByEntity(entity);
            var session = sessionFactory.getCurrentSession();

            session.beginTransaction();

            var result = session.createQuery(criteriaQuery).getResultList();

            session.getTransaction().commit();

            return result;
        }
    }
}
