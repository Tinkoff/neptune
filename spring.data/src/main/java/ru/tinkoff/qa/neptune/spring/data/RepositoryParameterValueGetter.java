package ru.tinkoff.qa.neptune.spring.data;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

public class RepositoryParameterValueGetter implements ParameterValueGetter<Repository<?, ?>> {

    @Override
    public String getParameterValue(Repository<?, ?> fieldValue) {
        var repoClass = fieldValue.getClass();
        var stBuilder = new StringBuilder(repoClass.getName());
        if (isLoggable(fieldValue)) {
            stBuilder.append("; ").append(fieldValue);
        }
        return stBuilder.toString();
    }
}
