package ru.tinkoff.qa.neptune.data.base.api.queries.ids;

import static java.util.Optional.ofNullable;

/**
 * The utility class that helps to build a query by known id
 */
public final class Id extends IdQueryBuilder {

    private Id(Object id) {
        super(ofNullable(id)
                .map(o -> new Object[] {o})
                .orElse(null));
    }

    /**
     * Creates an instance that wraps id of a stored object to find it in the data store.
     *
     * @param id is an id used to select desired object
     * @return a new {@link Id}
     */
    public static Id ids(Object id) {
        return new Id(id);
    }
}
