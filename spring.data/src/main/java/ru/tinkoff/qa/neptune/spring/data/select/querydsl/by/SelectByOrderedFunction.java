package ru.tinkoff.qa.neptune.spring.data.select.querydsl.by;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Description("By order specifiers")
@SuppressWarnings("unchecked")
public final class SelectByOrderedFunction<R, ID, T extends Repository<R, ID>>
    extends SpringDataFunction<T, Iterable<R>> {

    private Predicate predicate;
    private OrderSpecifier<?>[] orderSpecifiers;

    public SelectByOrderedFunction() {
        super(QuerydslPredicateExecutor.class, ReactiveQuerydslPredicateExecutor.class);
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public void setOrderSpecifiers(OrderSpecifier<?>... orders) {
        checkNotNull(orders);
        checkArgument(orders.length > 0, "At least one order specifier should be defined");
        this.orderSpecifiers = orders;
    }

    @Override
    public Iterable<R> apply(T t) {
        checkState(nonNull(orderSpecifiers) && orderSpecifiers.length > 0, "At least one order specifier should be defined");
        return ofNullable(predicate)
            .map(p -> {
                if (t instanceof QuerydslPredicateExecutor) {
                    return ((QuerydslPredicateExecutor<R>) t).findAll(predicate, orderSpecifiers);
                }

                if (t instanceof ReactiveQuerydslPredicateExecutor) {
                    return ((ReactiveQuerydslPredicateExecutor<R>) t).findAll(predicate, orderSpecifiers).collectList().block();
                }

                throw unsupportedRepository(t);
            })
            .orElseGet(() -> {
                if (t instanceof QuerydslPredicateExecutor) {
                    return ((QuerydslPredicateExecutor<R>) t).findAll(orderSpecifiers);
                }

                if (t instanceof ReactiveQuerydslPredicateExecutor) {
                    return ((ReactiveQuerydslPredicateExecutor<R>) t).findAll(orderSpecifiers).collectList().block();
                }

                throw unsupportedRepository(t);
            });
    }
}
