package ru.tinkoff.qa.neptune.data.base.api.queries.ids;

/**
 * The utility class that helps to build a query by known ids
 */
public final class Ids extends IdQueryBuilder {

    private Ids(Object[] ids) {
        super(ids);
    }

    /**
     * Creates an instance that wraps ids of stored objects to find them in the data store.
     *
     * @param ids is an array of ids used to select desired objects
     * @return a new {@link Ids}
     */
    public static Ids ids(Object... ids) {
        return new Ids(ids);
    }
}
