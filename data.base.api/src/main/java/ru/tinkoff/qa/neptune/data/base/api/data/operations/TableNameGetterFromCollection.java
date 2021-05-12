package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.Collection;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.data.base.api.PersistableObject.getTable;

public class TableNameGetterFromCollection implements ParameterValueGetter<Collection<PersistableObject>> {

    @Override
    public String getParameterValue(Collection<PersistableObject> fieldValue) {
        return fieldValue.stream().map(p -> getTable(p.getClass()))
                .map(s -> {
                    if (isNotBlank(s)) {
                        return "'" + s + "'";
                    } else {
                        return "<No table information>";
                    }
                })
                .distinct()
                .collect(joining(", "));
    }
}
