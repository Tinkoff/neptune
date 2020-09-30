package ru.tinkoff.qa.neptune.http.api.properties.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

@PropertyDescription(description = {"Defines full name of a class which implements Supplier<ObjectMapper> and whose objects",
        "supply mappers for serialization/deserialization of json strings. This may be necessary",
        "for default serialization/deserialization rules"},
        section = "Http client. Mappers")
@PropertyName("DEFAULT_JSON_MAPPER")
public final class DefaultJsonObjectMapper implements ObjectPropertySupplier<Supplier<ObjectMapper>> {

    public static final DefaultJsonObjectMapper DEFAULT_JSON_OBJECT_MAPPER = new DefaultJsonObjectMapper();

    private DefaultJsonObjectMapper() {
        super();
    }
}
