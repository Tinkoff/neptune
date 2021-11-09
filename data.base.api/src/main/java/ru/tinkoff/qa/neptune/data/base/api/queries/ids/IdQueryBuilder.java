package ru.tinkoff.qa.neptune.data.base.api.queries.ids;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.KeepResultPersistent;
import ru.tinkoff.qa.neptune.data.base.api.queries.Query;

import java.util.List;
import java.util.StringJoiner;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

/**
 * The utility class that helps to build a query by known ids
 */
@Deprecated(forRemoval = true)
abstract class IdQueryBuilder {

    private final Object[] ids;

    IdQueryBuilder(Object[] ids) {
        this.ids = ids;
    }

    /**
     * The method builds a query that selects stored objects by defined ids
     *
     * @param toSelect is a class of {@link PersistableObject} to be selected
     * @param <T>      is a type of {@link PersistableObject} to be selected
     * @return a function that performs the selecting of stored objects by defined ids.
     */
    public <T extends PersistableObject> Query<T, List<T>> build(Class<T> toSelect,
                                                                 KeepResultPersistent keepResultPersistent) {
        return new IdQuery<>(toSelect, ids, keepResultPersistent);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "[", "]")
                .add(stream(ids).map(Object::toString).collect(joining(",")))
                .toString();
    }
}
