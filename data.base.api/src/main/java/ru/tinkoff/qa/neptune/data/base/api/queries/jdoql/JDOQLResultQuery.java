package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import ru.tinkoff.qa.neptune.data.base.api.ListOfDataBaseObjects;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.List.copyOf;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class JDOQLResultQuery<T extends PersistableObject> implements Function<ReadableJDOQuery<T>, List<List<Object>>> {

    private JDOQLResultQuery() {
        super();
    }

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
            if (o != null && o.getClass().isArray()) {
                toBeReturned.add(copyOf(asList(((Object[]) o)))
                        .stream()
                        .map(o1 -> ofNullable(o1).map(o2 -> {
                            if (!PersistableObject.class.isAssignableFrom(o2.getClass())) {
                                return o2;
                            }
                            else {
                                return manager.detachCopy(o2);
                            }
                        }).orElse(null))
                        .collect(toList()));
            } else {
                var resulted = ofNullable(o).map(o1 -> {
                    if (!PersistableObject.class.isAssignableFrom(o.getClass())) {
                        return o;
                    }
                    else {
                        return manager.detachCopy(o);
                    }
                }).orElse(null);
                var toAdd = new ArrayList<>();
                toAdd.add(resulted);

                toBeReturned.add(toAdd);
            }
        });

        return new ListOfDataBaseObjects<>(toBeReturned) {
            public String toString() {
                return format("%s selected by sql query '%s' ",
                        size(),
                        jdoqlTypedQuery.getInternalQuery());
            }
        };
    }
}
