package ru.tinkoff.qa.neptune.hibernate.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import javax.persistence.criteria.Order;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;

@SuppressWarnings("unchecked")
@Description("all by ordering")
public final class SelectionByOrder<R> implements Function<Class<?>, Iterable<R>> {

    final List<Order> orders;

    public SelectionByOrder(List<Order> orders) {
        checkNotNull(orders);
        this.orders = orders;
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(Class<?> t) {
        var sessionFactory = getSessionFactoryByEntity(t);
        var session = sessionFactory.getCurrentSession();
        var criteriaBuilder = session.getCriteriaBuilder();
        var criteria = criteriaBuilder.createQuery(t).orderBy(orders);

        return (List<R>) session.createQuery(criteria).getResultList();
    }
}
