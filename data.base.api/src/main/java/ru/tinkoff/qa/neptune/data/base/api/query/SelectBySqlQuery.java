package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.ListOfStoredObjects.INFO_PERSISTABLE_INFO;

@SuppressWarnings("unchecked")
@Deprecated
class SelectBySqlQuery<T> implements Function<SQLQuery<T>, List<T>> {

    @Override
    public List<T> apply(SQLQuery<T> sqlQuery) {
        var query = sqlQuery.getQuery();
        var clazz = query.getInternalQuery().getCandidateClass();

        return ofNullable(clazz).map(tClass -> {
            if (PersistableObject.class.isAssignableFrom(tClass)) {
                return new ListOfSelectObjects<>(query.executeList(),
                        ts -> INFO_PERSISTABLE_INFO.apply((List<PersistableObject>) ts))
                        .setQuery(sqlQuery);
            }

            return (List<T>) getUntypedResult(sqlQuery);
        }).orElseGet(() -> (List<T>) getUntypedResult(sqlQuery));
    }

    private static List<List<Object>> getUntypedResult(SQLQuery<?> sqlQuery) {
        var toBeReturned = new ArrayList<>();
        var result = sqlQuery.getQuery().executeList();

        result.forEach(o -> {
            if (o.getClass().isArray()) {
                toBeReturned.add(new ListOfSelectObjects("1 record/value from the data base", Arrays.asList(((Object[]) o)))
                        .setQuery(sqlQuery));
            } else {
                toBeReturned.add(new ListOfSelectObjects("1 record/value from the data base", of(o)));
            }
        });
        return new ListOfSelectObjects("List of records/values from the data base",
                toBeReturned).setQuery(sqlQuery);
    }
}
