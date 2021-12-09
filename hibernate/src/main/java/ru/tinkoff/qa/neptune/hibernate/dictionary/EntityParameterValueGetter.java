package ru.tinkoff.qa.neptune.hibernate.dictionary;

import org.hibernate.SessionFactory;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

public class EntityParameterValueGetter implements ParameterValueGetter<Class<?>> {

    @Override
    public String getParameterValue(Class<?> clazz) {
        var stBuilder = new StringBuilder(clazz.getName());
        if (isLoggable(clazz)) {
            stBuilder.append("; ").append(clazz);
        }
        return stBuilder.toString();
    }
}
