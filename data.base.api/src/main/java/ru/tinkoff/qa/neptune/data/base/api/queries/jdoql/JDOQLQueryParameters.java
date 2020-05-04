package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.PersistableExpression;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

/**
 * This class is designed to construct/detail {@link JDOQLTypedQuery} to select list of stored objects
 *
 * @param <T> is a type of {@link PersistableObject} to be selected
 * @param <Q> is a type of {@link PersistableExpression} that represents {@code T} in a query
 */
public final class JDOQLQueryParameters<T extends PersistableObject, Q extends PersistableExpression<T>>
        extends JDOParameters<T, Q, JDOQLQueryParameters<T, Q>> {

    private JDOQLQueryParameters(Class<? extends Q> toUse) {
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
     * @param <T> is a type of {@link PersistableObject} to be selected.
     * @param <Q> is a type of {@link PersistableExpression} that represents {@code T} in a query
     * @return new {@link JDOQLQueryParameters}
     */
    public static <T extends PersistableObject, Q extends PersistableExpression<T>> JDOQLQueryParameters<T, Q> byJDOQuery(Class<Q> toUse) {
        return new JDOQLQueryParameters<>(toUse);
    }

    public <M extends JDOQLTypedQuery<T>> M  buildQuery(M m) {
        ofNullable(where).ifPresent(m::filter);

        if (nonNull(rangeStart) && nonNull(rangeEnd)) {
            m.range(rangeStart, rangeEnd);
        }

        ofNullable(orderExpressions)
                .filter(oe -> oe.length > 0)
                .ifPresent(m::orderBy);

        return m;
    }
}
