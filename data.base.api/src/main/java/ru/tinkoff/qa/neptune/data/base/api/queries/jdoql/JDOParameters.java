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
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.WhereJunction.and;

/**
 * This class is designed to construct/detail {@link JDOQLTypedQuery}
 *
 * @param <T> is a type of {@link PersistableObject} to be selected
 * @param <Q> is a type of {@link PersistableExpression} that represents {@code T} in a query
 */
@Deprecated(forRemoval = true)
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
     * Adds WHERE-clause to query. The sample below:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .addWhere(qPerson -> qPerson.name.eq(desiredName))}
     * </p>
     *
     * It is possible to define several expressions. The sample below demonstrates how define few expressions
     * aggregated by AND-junction:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .addWhere(qPerson -> qPerson.name.eq(desiredName))
     *          .addWhere(qPerson -> qPerson.age.gt(age))
     *          }
     * </p>
     *
     * Also it is possible to use junctions.
     * @see WhereJunction
     * The sample below:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .addWhere(qPerson -> or(
     *                qPerson.name.eq(desiredName),
     *                qPerson.age.gt(age)))
     *                }
     * </p>
     *
     * @param whereExpressions are functions that return a {@link BooleanExpression} on the applying.
     * @return self-reference
     */
    public final S addWhere(Function<Q, BooleanExpression> whereExpressions) {
        checkArgument(nonNull(whereExpressions), "Where-expression should be defined as not a null value");
        var whereExpr = whereExpressions.apply(persistableExpression);
        checkNotNull(whereExpr, "A new added `where` is null");

        this.where = ofNullable(this.where).map(booleanExpression -> and(booleanExpression, whereExpr))
                .orElse(whereExpr);
        return (S) this;
    }

    /**
     * Adds ORDER BY-clause to query. The sample below:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .orderBy(qPerson -> qPerson.id.asc())
     *          .orderBy(qPerson -> qPerson.birthDay.asc())}
     * </p>
     * @param orderExpression is a function that returns a {@link OrderExpression}
     * @return self-reference
     */
    public S orderBy(Function<Q, OrderExpression<?>> orderExpression) {
        checkArgument(nonNull(orderExpression), "Order expression should be defined as not a null value");
        var orderBy = orderExpression.apply(persistableExpression);
        checkNotNull(orderBy);
        this.orderExpressions = ofNullable(this.orderExpressions)
                .map(orderExpressions1 -> add(orderExpressions1, orderBy))
                .orElseGet(() -> new OrderExpression[]{orderBy});
        return (S) this;
    }

    /**
     * Sets range of resulted row numbers to be returned by the selecting.
     *
     * @param rangeStart number of the first row to be returned
     * @param rangeEnd number of the first row to be returned
     * @return self-reference
     */
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
     * @param m   an instance of {@link JDOQLTypedQuery} to be constructed/detailed
     * @param <M> is a type of {@link JDOQLTypedQuery} to be constructed/detailed
     * @return constructed/detailed query
     */
    abstract <M extends JDOQLTypedQuery<T>> M buildQuery(M m);
}
