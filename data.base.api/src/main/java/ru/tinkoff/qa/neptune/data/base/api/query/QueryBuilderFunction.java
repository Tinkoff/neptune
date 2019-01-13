package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.query.BooleanExpression;
import javax.jdo.query.Expression;
import javax.jdo.query.OrderExpression;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

/**
 * This function builds a typed JDO query.
 *
 * @param <T> type of objects to be selected.
 */
public final class QueryBuilderFunction<T extends PersistableObject> implements Function<DataBaseStepContext, JDOQLTypedQuery<T>> {

    private final Class<T> toSelect;
    private BooleanExpression constraintsExpression;
    private OrderExpression<?>[] orderExpressions;
    private Expression<?>[] groupByExpressions;
    private Expression<?> havingExpression;
    private Integer rangeStart;
    private Integer rangeEnd;

    /**
     * Creates an instance of function that builds a typed JDO query.
     *
     * @param toSelect is a class of objects to be selected by query.
     * @param <T> is a type of objects to be selected by query.
     * @return a function that builds a typed JDO query.
     */
    public static <T extends PersistableObject> QueryBuilderFunction<T> ofType(Class<T> toSelect) {
        return new QueryBuilderFunction<>(toSelect);
    }

    private QueryBuilderFunction(Class<T> toSelect) {
        checkArgument(nonNull(toSelect), "Class of persistable objects to select should be defined");
        checkArgument(nonNull(toSelect.getAnnotation(PersistenceCapable.class)), format("Class of persistable objects to select " +
                "should be annotated by %s", PersistenceCapable.class.getName()));
        this.toSelect = toSelect;
    }

    public QueryBuilderFunction<T> where(BooleanExpression constraintsExpression) {
        checkArgument(nonNull(constraintsExpression), "Constraint expression should be defined");
        this.constraintsExpression = constraintsExpression;
        return this;
    }

    public QueryBuilderFunction<T> range(int rangeStart, int rangeEnd) {
        checkArgument(rangeStart >= 0, "A number of the range starting should be positive or 0");
        checkArgument(rangeEnd > 0, "A number of the range ending should be greater than 0");
        checkArgument(rangeEnd > rangeStart, "The number of the range ending should be greater than " +
                "the number of the range starting");
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        return this;
    }

    public QueryBuilderFunction<T> groupBy(Expression<?>... groupByExpressions) {
        checkArgument(nonNull(groupByExpressions), "Grouping expression should be defined as not a null value");
        checkArgument(groupByExpressions.length > 0, "At least one grouping expression is expected to be defined");
        this.groupByExpressions = groupByExpressions;
        return this;
    }

    public QueryBuilderFunction<T> having(Expression<?> havingExpression) {
        checkArgument(nonNull(havingExpression), "Having expression should be defined as not a null value");
        this.havingExpression = havingExpression;
        return this;
    }

    public QueryBuilderFunction<T> orderBy(OrderExpression<?>... orderExpressions) {
        checkArgument(nonNull(orderExpressions), "Order expression should be defined as not a null value");
        checkArgument(orderExpressions.length > 0, "At least one order expression is expected to be defined");
        this.orderExpressions = orderExpressions;
        return this;
    }

    public Class<T> getTypeOfItemToSelect() {
        return toSelect;
    }

    @Override
    public JDOQLTypedQuery<T> apply(DataBaseStepContext dataBaseSteps) {
        var manager = dataBaseSteps.getCurrentPersistenceManager();
        var tq1 = manager.newJDOQLTypedQuery(toSelect);
        ofNullable(constraintsExpression).ifPresent(tq1::filter);
        if (nonNull(rangeStart) && nonNull(rangeEnd)) {
            tq1.range(rangeStart, rangeEnd);
        }
        ofNullable(orderExpressions).ifPresent(tq1::orderBy);
        ofNullable(groupByExpressions).ifPresent(tq1::groupBy);
        ofNullable(havingExpression).ifPresent(tq1::having);
        return tq1;
    }
}
