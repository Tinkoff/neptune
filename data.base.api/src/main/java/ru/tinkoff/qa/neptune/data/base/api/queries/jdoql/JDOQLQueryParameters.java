package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.BooleanExpression;
import javax.jdo.query.Expression;
import javax.jdo.query.OrderExpression;
import javax.jdo.query.PersistableExpression;
import java.lang.reflect.Method;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.add;

public final class JDOQLQueryParameters<T extends PersistableObject, Q extends PersistableExpression<T>> {

    private final Q persistableExpression;

    private BooleanExpression where;
    private OrderExpression<?>[] orderExpressions;
    private Expression<?>[] groupByExpressions;
    private Expression<?> havingExpression;
    private Integer rangeStart;
    private Integer rangeEnd;

    private JDOQLQueryParameters(Class<? extends Q> toUse) {
        this.persistableExpression = getCandidate(toUse);
    }

    public static <T extends PersistableObject, Q extends PersistableExpression<T>> JDOQLQueryParameters<T, Q> byJDOQuery(Class<? extends Q> toUse) {
        return new JDOQLQueryParameters<>(toUse);
    }


    @SuppressWarnings("unchecked")
    private static <T extends PersistableObject, Q extends PersistableExpression<T>> Q getCandidate(Class<Q> toUse) {
        checkNotNull(toUse);
        Method candidateMethod;
        try {
            candidateMethod = toUse.getDeclaredMethod("candidate");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(format("It seems that %s has no declared method 'candidate' with empty signature",
                    toUse.getName()), e);
        }

        var modifiers = candidateMethod.getModifiers();
        if (!isStatic(modifiers)) {
            throw new RuntimeException(new NoSuchMethodException(format("The 'candidate' should be static. The %s#%s is not",
                    candidateMethod.getDeclaringClass().getName(), candidateMethod.getName())));
        }

        try {
            return (Q) candidateMethod.invoke(toUse);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JDOQLQueryParameters<T, Q> where(Function<Q, BooleanExpression> whereExpression) {
        checkArgument(nonNull(whereExpression), "Where-expression should be defined as not a null value");
        var where = whereExpression.apply(persistableExpression);
        checkNotNull(where);
        this.where = where;
        return this;
    }

    OrderExpression<?>[] orderExpressions() {
        return orderExpressions;
    }

    public JDOQLQueryParameters<T, Q> setOrderBy(Function<Q, OrderExpression<?>> orderExpression) {
        checkArgument(nonNull(orderExpression), "Order expression should be defined as not a null value");
        var orderBy = orderExpression.apply(persistableExpression);
        checkNotNull(orderBy);
        this.orderExpressions = new OrderExpression[] {orderBy};
        return this;
    }

    public JDOQLQueryParameters<T, Q>  addOrderBy(Function<Q, OrderExpression<?>> orderExpression) {
        checkArgument(nonNull(orderExpression), "Order expression should be defined as not a null value");
        var orderBy = orderExpression.apply(persistableExpression);
        checkNotNull(orderBy);
        this.orderExpressions = ofNullable(this.orderExpressions)
                .map(orderExpressions1 -> add(orderExpressions1, orderBy))
                .orElseGet(() -> new OrderExpression[]{orderBy});
        return this;
    }

    public JDOQLQueryParameters<T, Q> setGroupBy(Function<Q, Expression<?>> expression) {
        checkArgument(nonNull(expression), "GroupBy expressions should be defined as not a null value");
        var groupBy = expression.apply(persistableExpression);
        checkNotNull(groupBy);
        this.groupByExpressions =  new Expression[] {groupBy};
        return this;
    }

    public void addGroupBy(Function<Q, Expression<?>> expression) {
        checkArgument(nonNull(expression), "GroupBy expression should be defined as not a null value");
        var groupBy = expression.apply(persistableExpression);
        checkNotNull(groupBy);
        this.groupByExpressions = ofNullable(this.groupByExpressions)
                .map(expressions -> add(expressions, groupBy))
                .orElseGet(() -> new Expression[]{groupBy});
    }

    public JDOQLQueryParameters<T, Q> having(Function<Q, Expression<?>> expression) {
        checkArgument(nonNull(expression), "Having expression should be defined as not a null value");
        var having = expression.apply(persistableExpression);
        checkNotNull(having);
        this.havingExpression = having;
        return this;
    }

    public JDOQLQueryParameters<T, Q> range(int rangeStart, int rangeEnd) {
        checkArgument(rangeStart >= 0, "A number of the range starting should be positive or 0");
        checkArgument(rangeEnd > 0, "A number of the range ending should be greater than 0");
        checkArgument(rangeEnd > rangeStart, "The number of the range ending should be greater than " +
                "the number of the range starting");
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        return this;
    }

    public <M extends JDOQLTypedQuery<T>> M  buildQuery(M m) {
        ofNullable(where).ifPresent(m::filter);

        if (nonNull(rangeStart) && nonNull(rangeEnd)) {
            m.range(rangeStart, rangeEnd);
        }

        ofNullable(orderExpressions)
                .ifPresent(orderExpressions1 -> {
                    if (orderExpressions1.length > 0) {
                        m.orderBy(orderExpressions1);
                    }
                });

        ofNullable(groupByExpressions)
                .ifPresent(expressions -> {
                    if (expressions.length > 0) {
                        m.groupBy(expressions);
                    }
                });

        ofNullable(havingExpression)
                .ifPresent(m::having);

        return m;
    }
}
