package ru.tinkoff.qa.neptune.spring.data.dictionary;

import com.querydsl.core.types.Predicate;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static ru.tinkoff.qa.neptune.spring.data.data.serializer.DataSerializer.serializeObject;

public class PredicateParameterValueGetter implements ParameterValueGetter<Predicate> {

    @Override
    public String getParameterValue(Predicate fieldValue) {
        return serializeObject(NON_NULL, fieldValue);
    }
}
