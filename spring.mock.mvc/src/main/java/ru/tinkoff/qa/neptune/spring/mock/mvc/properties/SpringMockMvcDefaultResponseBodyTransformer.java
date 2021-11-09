package ru.tinkoff.qa.neptune.spring.mock.mvc.properties;

import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;

@PropertyDescription(description = {"Defines default serialization and deserialization",
        "of data taken from bodies of http responses"},
        section = "Spring Mock MVC. Serialization and deserialization of bodies of http responses")
@PropertyName("SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER")
public final class SpringMockMvcDefaultResponseBodyTransformer implements ObjectByClassPropertySupplier<DataTransformer> {

    public static final SpringMockMvcDefaultResponseBodyTransformer SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER =
            new SpringMockMvcDefaultResponseBodyTransformer();

    private SpringMockMvcDefaultResponseBodyTransformer() {
        super();
    }

    @Override
    public DataTransformer get() {
        var result = ObjectByClassPropertySupplier.super.get();
        checkState(nonNull(result), "Please define property/env. variable '" + getName() + "'");
        return result;
    }
}