package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.HibernateFunction;

import javax.persistence.criteria.Order;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("all by ordering")
public final class SelectionByOrder<R> extends HibernateFunction<R, Iterable<R>> {

    final List<Order> orders;

    public SelectionByOrder(Class<R> entity, List<Order> orders) {
        super(entity);
        checkNotNull(orders);
        this.orders = orders;
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(HibernateContext context) {
        var sessionFactory = context.getSessionFactoryByEntity(entity);
        var session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        var criteriaBuilder = session.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(entity).orderBy(orders);
        var root = criteriaQuery.from(entity);

        var result = session.createQuery(criteriaQuery.select(root)).getResultList();

        session.getTransaction().commit();

        return result;
    }
}
