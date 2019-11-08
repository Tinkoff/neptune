package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.ListOfDataBaseObjects;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * This class is designed to perform a query to select list of list. Each list item contains values of fields taken
 * from stored objects.
 * @param <T> is a type of {@link PersistableObject} objects to take field values from
 */
public class JDOQLResultQuery<T extends PersistableObject> implements Function<ReadableJDOQuery<T>, List<List<Object>>>, IdSetter {

    private JDOQLResultQuery() {
        super();
    }

    /**
     * Creates an instance that performs a query to select list of field values taken from stored objects by {@link JDOQLTypedQuery}
     *
     * @param <T> is a type of {@link PersistableObject} objects to take field values from
     * @return new {@link JDOQLResultQuery}
     */
    public static <T extends PersistableObject> JDOQLResultQuery<T> byJDOQLResultQuery() {
        return new JDOQLResultQuery<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<List<Object>> apply(ReadableJDOQuery<T> jdoqlTypedQuery) {
        List<Object> list = jdoqlTypedQuery.executeResultList();
        var manager = jdoqlTypedQuery.getPersistenceManager();
        List<List<Object>> toBeReturned = new ArrayList<>();

        list.forEach(o -> {
            var row = new ListOfDataBaseObjects<>(){
                @Override
                public String toString() {
                    return format("row of field values: %s", super.toString());
                }
            };

            if (o != null && o.getClass().isArray()) {
                row.addAll(stream(((Object[]) o))
                        .map(o1 -> ofNullable(o1).map(o2 -> {
                            if (!PersistableObject.class.isAssignableFrom(o2.getClass())) {
                                return o2;
                            }
                            else {
                                var toReturn = manager.detachCopy(o2);
                                setRealId(o2, toReturn);
                                return toReturn;
                            }
                        }).orElse(null))
                        .collect(toList()));

                toBeReturned.add(row);
            } else {
                var resulted = ofNullable(o).map(o1 -> {
                    if (!PersistableObject.class.isAssignableFrom(o1.getClass())) {
                        return o1;
                    }
                    else {
                        var toReturn = manager.detachCopy(o1);
                        setRealId(o1, toReturn);
                        return toReturn;
                    }
                }).orElse(null);
                row.add(resulted);

                toBeReturned.add(row);
            }
        });

        return new ListOfDataBaseObjects<>(toBeReturned) {
            public String toString() {
                return format("%s row/rows selected by query '%s' ",
                        size(),
                        jdoqlTypedQuery.getInternalQuery());
            }
        };
    }
}
