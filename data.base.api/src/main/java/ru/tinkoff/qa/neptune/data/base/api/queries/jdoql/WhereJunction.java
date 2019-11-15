package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import org.datanucleus.api.jdo.query.BooleanExpressionImpl;
import org.datanucleus.api.jdo.query.ExpressionImpl;
import org.datanucleus.query.expression.DyadicExpression;
import org.datanucleus.query.expression.Expression;

import javax.jdo.query.BooleanExpression;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.datanucleus.query.expression.Expression.OP_AND;
import static org.datanucleus.query.expression.Expression.OP_OR;

/**
 * This is the utility class that helps to aggregate complex where-expressions
 * by AND-/OR junctions.
 */
public final class WhereJunction {

    private final Expression.DyadicOperator operator;

    private WhereJunction(Expression.DyadicOperator operator) {
        this.operator = operator;
    }

    /**
     * Aggregates defined expressions by AND-junction
     *
     * @param expressions expressions to be junctioned.
     * @return new {@link BooleanExpression}
     */
    public static BooleanExpression and(BooleanExpression... expressions) {
        return new WhereJunction(OP_AND).getExpression(expressions);
    }

    /**
     * Aggregates defined expressions by OR-junction
     *
     * @param expressions expressions to be junctioned.
     * @return new {@link BooleanExpression}
     */
    public static BooleanExpression or(BooleanExpression... expressions) {
        return new WhereJunction(OP_OR).getExpression(expressions);
    }

    private BooleanExpression getExpression(BooleanExpression... expressions) {
        checkNotNull(expressions, "Expressions should be defined as a value that differs from null");
        checkArgument(expressions.length >= 2, "At least 2 expression should be defined");

        BooleanExpression resultExpression = null;

        for (var be: expressions) {
            resultExpression = ofNullable(resultExpression)
                    .map(expr ->  {
                        Expression leftQueryExpr = ((ExpressionImpl) expr).getQueryExpression();
                        Expression rightQueryExpr = ((ExpressionImpl) be).getQueryExpression();

                        org.datanucleus.query.expression.Expression queryExpr =
                                new DyadicExpression(leftQueryExpr, operator, rightQueryExpr);
                        return (BooleanExpression) new BooleanExpressionImpl(queryExpr);
                    })
                    .orElse(be);
        }

        return resultExpression;
    }
}
