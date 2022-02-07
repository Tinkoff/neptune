package ru.tinkoff.qa.neptune.hibernate.select.querydsl.by;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.hibernate.HibernateQuery;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.HibernateFunction;

import java.util.List;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("By order specifiers")
@SuppressWarnings("unchecked")
public final class SelectByOrderedFunction<R> extends HibernateFunction<R, Iterable<R>> {

    private final EntityPath<?> entityPath;
    private Predicate predicate;
    private OrderSpecifier<?>[] orderSpecifiers;

    public SelectByOrderedFunction(Class<R> entity, EntityPath<?> entityPath) {
        super(entity);
        checkNotNull(entityPath);
        this.entityPath = entityPath;
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
    public Iterable<R> apply(HibernateContext context) {
        checkState(nonNull(orderSpecifiers) && orderSpecifiers.length > 0, "At least one order specifier should be defined");

        var sessionFactory = context.getSessionFactoryByEntity(entity);
        var session = sessionFactory.getCurrentSession();

        HibernateQuery<?> query = new HibernateQuery<>(session).from(entityPath);

        if (predicate != null) {
            query = query.select(predicate);
        }

        if (orderSpecifiers != null) {
            query = query.orderBy(orderSpecifiers);
        }

        return (List<R>) query.fetch();
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
