package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.Expression;
import javax.jdo.query.PersistableExpression;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.add;

/**
 * This class is designed to construct/detail {@link JDOQLTypedQuery} to select list of list. Each list item contains values of fields taken
 * from stored objects.
 *
 * @param <T> is a type of {@link PersistableObject} objects to take field values from
 * @param <Q> is a type of {@link PersistableExpression} that represents {@code T} in a query
 */
public class JDOQLResultQueryParams<T extends PersistableObject, Q extends PersistableExpression<T>>
        extends JDOParameters<T, Q, JDOQLResultQueryParams<T, Q>> {

    private Expression<?>[] groupByExpressions;
    private Expression<?> havingExpression;
    private Expression<?>[] resultFields;
    private boolean distinct;

    private JDOQLResultQueryParams(Class<? extends Q> toUse) {
        super(toUse);
    }

    /**
     * Creates an object that constructs/adds details to a query.
     *
     * @param toUse is a class of {@link PersistableExpression} that represents {@code T} in a query. It is important:
     *              <p>
     *              The class should have the accessible static {@code 'candidate()'} methods that creates/returns an instance
     *              </p>
     *
     * @param <T> is a type of {@link PersistableObject} to take field values from
     * @param <Q> is a type of {@link PersistableExpression} that represents {@code T} in a query
     * @return new {@link JDOQLResultQueryParams}
     */
    public static <T extends PersistableObject, Q extends PersistableExpression<T>> JDOQLResultQueryParams<T, Q> byJDOResultQuery(Class<Q> toUse) {
        return new JDOQLResultQueryParams<>(toUse);
    }

    /**
     * Sets GROUP BY-clause to query. The sample below:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .setGroupBy(qPerson -> qPerson.name)
     * </p>
     * @param expression is a function that returns a {@link Expression}
     * @return self-reference
     */
    public JDOQLResultQueryParams<T, Q> setGroupBy(Function<Q, Expression<?>> expression) {
        checkArgument(nonNull(expression), "GroupBy expressions should be defined as not a null value");
        var groupBy = expression.apply(persistableExpression);
        checkNotNull(groupBy);
        this.groupByExpressions =  new Expression[] {groupBy};
        return this;
    }

    /**
     * Adds GROUP BY-clause to query. The sample below:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .addGroupBy(qPerson -> qPerson.name)
     * </p>
     * @param expression is a function that returns a {@link Expression}
     * @return self-reference
     */
    public JDOQLResultQueryParams<T, Q> addGroupBy(Function<Q, Expression<?>> expression) {
        checkArgument(nonNull(expression), "GroupBy expression should be defined as not a null value");
        var groupBy = expression.apply(persistableExpression);
        checkNotNull(groupBy);
        this.groupByExpressions = ofNullable(this.groupByExpressions)
                .map(expressions -> add(expressions, groupBy))
                .orElseGet(() -> new Expression[]{groupBy});
        return this;
    }


    /**
     * Sets HAVING-clause to query. The sample below:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .having(qPerson -> qPerson.name)
     * </p>
     * @param expression is a function that returns a {@link Expression}
     * @return self-reference
     */
    public JDOQLResultQueryParams<T, Q> having(Function<Q, Expression<?>> expression) {
        checkArgument(nonNull(expression), "Having expression should be defined as not a null value");
        var having = expression.apply(persistableExpression);
        checkNotNull(having);
        this.havingExpression = having;
        return this;
    }

    /**
     * Sets result field to query. The sample below:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .setResultField(qPerson -> qPerson.name)
     * </p>
     * @param expression is a function that returns a {@link Expression}
     * @return self-reference
     */
    public JDOQLResultQueryParams<T, Q> setResultField(Function<Q, Expression<?>> expression) {
        checkArgument(nonNull(expression), "Result field expression should be defined as not a null value");
        var result = expression.apply(persistableExpression);
        checkNotNull(result);
        this.resultFields =  new Expression[] {result};
        return this;
    }


    /**
     * Adds result field to query. The sample below:
     * <p>
     * {@code byJDOQuery(QPerson.class)
     *          .addResultField(qPerson -> qPerson.name)
     * </p>
     * @param expression is a function that returns a {@link Expression}
     * @return self-reference
     */
    public JDOQLResultQueryParams<T, Q> addResultField(Function<Q, Expression<?>> expression) {
        checkArgument(nonNull(expression), "Result field expression should be defined as not a null value");
        var result = expression.apply(persistableExpression);
        checkNotNull(result);
        this.resultFields = ofNullable(this.resultFields)
                .map(expressions -> add(expressions, result))
                .orElseGet(() -> new Expression[]{result});
        return this;
    }

    /**
     * Defines whenever to select distinct results or not.
     *
     * @param distinct to select distinct results or not
     * @return self-reference
     */
    public JDOQLResultQueryParams<T, Q> distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    @Override
    public <M extends JDOQLTypedQuery<T>> M  buildQuery(M m) {
        checkArgument(nonNull(resultFields) && resultFields.length > 0,
                "Should be defined at least one resul field");

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

        m.result(distinct, resultFields);

        return m;
    }
}
