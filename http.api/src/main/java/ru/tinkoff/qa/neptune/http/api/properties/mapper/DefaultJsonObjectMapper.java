package ru.tinkoff.qa.neptune.http.api.properties.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

/**
 * This class is designed to read value of the property {@code 'DEFAULT_JSON_MAPPER'} and convert it to an instance of
 * a {@link Supplier} that creates {@link ObjectMapper}
 */
public final class DefaultJsonObjectMapper implements ObjectPropertySupplier<Supplier<ObjectMapper>> {

    public static final DefaultJsonObjectMapper DEFAULT_JSON_OBJECT_MAPPER = new DefaultJsonObjectMapper();
    private static final String PROPERTY = "DEFAULT_JSON_MAPPER";

    private DefaultJsonObjectMapper() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }
}
