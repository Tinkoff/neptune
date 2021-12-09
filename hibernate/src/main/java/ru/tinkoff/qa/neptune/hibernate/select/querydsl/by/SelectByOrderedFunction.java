package ru.tinkoff.qa.neptune.hibernate.select.querydsl.by;

import com.google.common.base.Function;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.hibernate.HibernateQuery;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.getSessionFactoryByEntity;

@Description("By order specifiers")
@SuppressWarnings("unchecked")
public final class SelectByOrderedFunction<R>
        implements Function<Class<?>, Iterable<R>> {

    private Predicate predicate;
    private OrderSpecifier<?>[] orderSpecifiers;

    public SelectByOrderedFunction() {
    }

    public void setPredicate(Predicate predicate) {
        checkNotNull(predicate);
        this.predicate = predicate;
    }

    public void setOrderSpecifiers(OrderSpecifier<?>... orders) {
        checkNotNull(orders);
        checkArgument(orders.length > 0, "At least one order specifier should be defined");
        this.orderSpecifiers = orders;
    }

    @Override
    public Iterable<R> apply(Class<?> t) {
        checkState(nonNull(orderSpecifiers) && orderSpecifiers.length > 0, "At least one order specifier should be defined");

        var sessionFactory = getSessionFactoryByEntity(t);
        var session = sessionFactory.getCurrentSession();

        return (List<R>) new HibernateQuery<>(session).select(predicate).orderBy(orderSpecifiers).fetch();
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
