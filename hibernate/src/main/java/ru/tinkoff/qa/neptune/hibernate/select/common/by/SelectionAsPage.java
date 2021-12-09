package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import javax.persistence.criteria.Order;
import java.util.List;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;

@Description("As page")
public final class SelectionAsPage<R> implements Function<Class<R>, Iterable<R>> {

    private int limit;
    private int offset;
    private List<Order> orders;

    public SelectionAsPage() {
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(Class<R> t) {
        var sessionFactory = getSessionFactoryByEntity(t);
        var session = sessionFactory.getCurrentSession();
        var criteriaBuilder = session.getCriteriaBuilder();

        var criteria = criteriaBuilder.createQuery(t);

        if (orders != null) {
            orders.forEach(order -> criteria.orderBy(orders));
        }

        var query = session.createQuery(criteria);

        if (limit != 0) {
            query.setMaxResults(limit);
        }

        if (offset != 0) {
            query.setFirstResult(offset);
        }

        return query.getResultList();
    }

    public void setLimitOffset(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public void setLimitOffset(int limit, int offset, List<Order> orders) {
        this.limit = limit;
        this.offset = offset;
        this.orders = orders;
    }
}
