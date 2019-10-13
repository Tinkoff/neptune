package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.ListOfDataBaseObjects;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

public final class JDOQLQuery<T extends PersistableObject> implements Function<JDOPersistenceManager, List<T>> {

    private final Class<T> classOfRequestedValue;
    private JDOQLQueryParameters<T, ?> parameters;

    private JDOQLQuery(Class<T> classOfRequestedValue) {
        checkNotNull(classOfRequestedValue);
        this.classOfRequestedValue = classOfRequestedValue;
    }

    public static <T extends PersistableObject> JDOQLQuery<T> byJDOQLQuery(Class<T> classOfRequestedValue) {
        return new JDOQLQuery<>(classOfRequestedValue);
    }

    @Override
    public List<T> apply(JDOPersistenceManager jdoPersistenceManager) {
        var query = jdoPersistenceManager.newJDOQLTypedQuery(classOfRequestedValue);

        ofNullable(parameters).ifPresent(queryParameters -> {
            ofNullable(queryParameters.getWhere()).ifPresent(query::filter);

            var startRange = queryParameters.getRangeStart();
            var endRange = queryParameters.getRangeEnd();

            if (nonNull(startRange) && nonNull(endRange)) {
                query.range(startRange, endRange);
            }

            ofNullable(queryParameters.orderExpressions())
                    .ifPresent(orderExpressions -> {
                        if (orderExpressions.length > 0) {
                            query.orderBy(orderExpressions);
                        }
                    });

            ofNullable(queryParameters.groupByExpressions())
                    .ifPresent(expressions -> {
                        if (expressions.length > 0) {
                            query.groupBy(expressions);
                        }
                    });

            ofNullable(queryParameters.havingExpression())
                    .ifPresent(query::having);
        });

        return new ListOfDataBaseObjects<>(query.executeList()) {
            public String toString() {
                return format("%s objects/object selected by jdo typed query %s", size(), query.toString());
            }
        };
    }

    public JDOQLQuery<T> setParameters(JDOQLQueryParameters<T, ?> parameters) {
        checkNotNull(parameters, "Parameters should be defined as not a null value");
        this.parameters = parameters;
        return this;
    }
}
