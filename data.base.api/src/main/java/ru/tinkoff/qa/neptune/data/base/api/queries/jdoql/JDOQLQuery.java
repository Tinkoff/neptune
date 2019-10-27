package ru.tinkoff.qa.neptune.data.base.api.queries.jdoql;

import org.apache.commons.lang3.StringUtils;
import ru.tinkoff.qa.neptune.data.base.api.ListOfDataBaseObjects;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.List;
import java.util.function.Function;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

public final class JDOQLQuery<T extends PersistableObject> implements Function<ReadableJDOQuery<T>, List<T>> {

    private JDOQLQuery() {
        super();
    }

    public static <T extends PersistableObject> JDOQLQuery<T> byJDOQLQuery() {
        return new JDOQLQuery<>();
    }

    @Override
    public List<T> apply(ReadableJDOQuery<T> jdoqlTypedQuery) {
        var list = jdoqlTypedQuery.executeList();
        var manager = jdoqlTypedQuery.getPersistenceManager();

        return new ListOfDataBaseObjects<>(manager.detachCopyAll(list)) {
            public String toString() {
                var resultStr =  format("%s objects/object", size());

                var tableList = stream().map(PersistableObject::fromTable)
                        .filter(StringUtils::isNotBlank)
                        .distinct()
                        .collect(toList());

                if (tableList.size() > 0) {
                    resultStr = format("%s of table/tables %s", resultStr, join(",", tableList));
                }

                return resultStr;
            }
        };
    }
}
