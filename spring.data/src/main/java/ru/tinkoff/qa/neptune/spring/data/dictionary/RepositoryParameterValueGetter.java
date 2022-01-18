package ru.tinkoff.qa.neptune.spring.data.dictionary;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.lang.reflect.Proxy;

import static java.util.Arrays.stream;

public class RepositoryParameterValueGetter implements ParameterValueGetter<Repository<?, ?>> {

    @Override
    public String getParameterValue(Repository<?, ?> fieldValue) {
        Class<?> repoClass;
        if (fieldValue instanceof Proxy) {
            repoClass = stream(fieldValue.getClass().getInterfaces())
                    .min((o1, o2) -> {
                        if (o1.equals(o2)) {
                            return 0;
                        }

                        if (o1.isAssignableFrom(o2)) {
                            return 1;
                        }

                        return -1;
                    }).orElseGet(fieldValue::getClass);
        } else {
            repoClass = fieldValue.getClass();
        }

        return repoClass.getName();
    }
}
