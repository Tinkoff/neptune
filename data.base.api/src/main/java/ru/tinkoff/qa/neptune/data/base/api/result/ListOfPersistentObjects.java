package ru.tinkoff.qa.neptune.data.base.api.result;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.data.base.api.PersistableObject.getTable;

/**
 * This collection contains only {@link PersistableObject}'s and provides specific string description.
 *
 * @param <T> is a type of items
 */
public class ListOfPersistentObjects<T extends PersistableObject> extends ArrayList<T> {

    protected ListOfPersistentObjects() {
        super();
    }

    protected ListOfPersistentObjects(Collection<T> from) {
        super(from);
    }

    @Override
    public String toString() {
        var resultStr =  format("%s objects/object", size());

        var tableList = stream().map(p -> getTable(p.getClass()))
                .map(s -> {
                    if (isNotBlank(s)) {
                        return s;
                    } else {
                        return "<No table information>";
                    }
                })
                .distinct()
                .collect(toList());

        if (tableList.size() > 0) {
            resultStr = format("%s of table/tables %s", resultStr, join(",", tableList));
        }

        return resultStr;
    }
}
