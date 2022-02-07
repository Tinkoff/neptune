package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.HibernateFunction;

import javax.persistence.criteria.Order;
import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("As page")
public final class SelectionAsPage<R> extends HibernateFunction<R, Iterable<R>> {

    private int limit;
    private int offset;
    private List<Order> orders;

    public SelectionAsPage(Class<R> entity) {
        super(entity);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(HibernateContext context) {
        var sessionFactory = context.getSessionFactoryByEntity(entity);
        var session = sessionFactory.getCurrentSession();
        var criteriaBuilder = session.getCriteriaBuilder();

        var criteria = criteriaBuilder.createQuery(entity);

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
