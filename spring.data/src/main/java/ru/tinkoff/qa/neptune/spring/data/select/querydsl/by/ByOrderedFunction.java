package ru.tinkoff.qa.neptune.spring.data.select.querydsl.by;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

public final class ByOrderedFunction<R, T extends QuerydslPredicateExecutor<R>>
        extends SpringDataFunction<T, Iterable<R>> {

    private Predicate predicate;
    private OrderSpecifier<?>[] orderSpecifiers;

    public ByOrderedFunction() {
        super(QuerydslPredicateExecutor.class);
    }

    public void setPredicate(Predicate predicate) {
        checkNotNull(predicate);
        this.predicate = predicate;
    }

    public void setPredicate(OrderSpecifier<?>... orders) {
        checkNotNull(orders);
        checkArgument(orders.length > 0, "At least one order specifier should be defined");
        this.orderSpecifiers = orders;
    }

    @Override
    public Iterable<R> apply(T t) {
        checkState(nonNull(orderSpecifiers) && orderSpecifiers.length > 0, "At least one order specifier should be defined");
        return ofNullable(predicate)
                .map(p -> t.findAll(p, orderSpecifiers))
                .orElseGet(() -> t.findAll(orderSpecifiers));
    }
}
