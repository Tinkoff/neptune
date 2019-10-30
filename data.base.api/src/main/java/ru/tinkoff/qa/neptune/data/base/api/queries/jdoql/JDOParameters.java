package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;


import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.BooleanExpression;
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

@SuppressWarnings("unchecked")
public abstract class JDOParameters<T extends PersistableObject, Q extends PersistableExpression<T>, S extends JDOParameters<T, Q, S>> {

    final Q persistableExpression;
    BooleanExpression where;
    OrderExpression<?>[] orderExpressions;
    Integer rangeStart;
    Integer rangeEnd;

    JDOParameters(Class<? extends Q> toUse) {
        this.persistableExpression = getCandidate(toUse);
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

    /**
     * Sets WHERE-clause to query. The sample below:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .where(qPerson -> qPerson.name.eq(desiredName))}
     * </p>
     * @param whereExpression is a function that returns a {@link BooleanExpression}
     * @return self-reference
     */
    public S where(Function<Q, BooleanExpression> whereExpression) {
        checkArgument(nonNull(whereExpression), "Where-expression should be defined as not a null value");
        var where = whereExpression.apply(persistableExpression);
        checkNotNull(where);
        this.where = where;
        return (S) this;
    }

    /**
     * Sets ORDER BY-clause to query. The sample below:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .setOrderBy(qPerson -> qPerson.id.asc())}
     * </p>
     * @param orderExpression is a function that returns a {@link OrderExpression}
     * @return self-reference
     */
    public S setOrderBy(Function<Q, OrderExpression<?>> orderExpression) {
        checkArgument(nonNull(orderExpression), "Order expression should be defined as not a null value");
        var orderBy = orderExpression.apply(persistableExpression);
        checkNotNull(orderBy);
        this.orderExpressions = new OrderExpression[] {orderBy};
        return (S) this;
    }

    /**
     * Adds ORDER BY to query. The sample below:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .addOrderBy(qPerson -> qPerson.id.asc())
     *          .addOrderBy(qPerson -> qPerson.birthDay.asc())}
     * </p>
     * @param orderExpression is a function that returns a {@link OrderExpression}
     * @return self-reference
     */
    public S  addOrderBy(Function<Q, OrderExpression<?>> orderExpression) {
        checkArgument(nonNull(orderExpression), "Order expression should be defined as not a null value");
        var orderBy = orderExpression.apply(persistableExpression);
        checkNotNull(orderBy);
        this.orderExpressions = ofNullable(this.orderExpressions)
                .map(orderExpressions1 -> add(orderExpressions1, orderBy))
                .orElseGet(() -> new OrderExpression[]{orderBy});
        return (S) this;
    }

    public S range(int rangeStart, int rangeEnd) {
        checkArgument(rangeStart >= 0, "A number of the range starting should be positive or 0");
        checkArgument(rangeEnd > 0, "A number of the range ending should be greater than 0");
        checkArgument(rangeEnd > rangeStart, "The number of the range ending should be greater than " +
                "the number of the range starting");
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        return (S) this;
    }

    /**
     * Constructs/Adds details to a query
     *
     * @param m an instance of {@link JDOQLTypedQuery} to be constructed/detailed
     * @param <M> is a type of {@link JDOQLTypedQuery} to be constructed/detailed
     * @return constructed/detailed query
     */
    public abstract <M extends JDOQLTypedQuery<T>> M  buildQuery(M m);
}
