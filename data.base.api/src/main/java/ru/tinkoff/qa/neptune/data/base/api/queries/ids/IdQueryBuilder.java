package ru.tinkoff.qa.neptune.data.base.api.queries.ids;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * The utility class that helps to build a query by known ids
 */
abstract class IdQueryBuilder {

    private final Object[] ids;

    IdQueryBuilder(Object[] ids) {
        this.ids = ids;
    }

    /**
     * The method builds a function that selects stored objects by defined ids
     *
     * @param toSelect is a class of {@link PersistableObject} to be selected
     * @param <T> is a type of {@link PersistableObject} to be selected
     * @return a function that performs the selecting of stored objects by defined ids.
     */
    public <T extends PersistableObject> Function<JDOPersistenceManager, List<T>> build(Class<T> toSelect) {
        return new IdQuery<>(toSelect, ids);
    }

    @Override
    public String toString() {
        return Arrays.toString(ids);
    }
}
