package ru.tinkoff.qa.neptune.data.base.api.queries;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import static ru.tinkoff.qa.neptune.data.base.api.PersistableObject.getTable;

@Deprecated(forRemoval = true)
public class TableNameGetter implements ParameterValueGetter<Class<? extends PersistableObject>> {

    @Override
    public String getParameterValue(Class<? extends PersistableObject> fieldValue) {
        return getTable(fieldValue);
    }
}
