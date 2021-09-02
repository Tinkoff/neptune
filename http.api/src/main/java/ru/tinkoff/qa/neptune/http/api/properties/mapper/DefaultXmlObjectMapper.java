package ru.tinkoff.qa.neptune.http.api.properties.mapper;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

/**
 * This is deprecated in favour of usage of {@link ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer}
 */
@Deprecated
@PropertyDescription(description = {"Defines full name of a class which implements Supplier<XmlMapper> and whose objects",
        "supply mappers for serialization/deserialization of xml strings. This may be necessary",
        "for default serialization/deserialization rules"},
        section = "Http client. Mappers")
@PropertyName("DEFAULT_XML_MAPPER")
public final class DefaultXmlObjectMapper implements ObjectPropertySupplier<XmlMapper, Supplier<XmlMapper>> {

    public static final DefaultXmlObjectMapper DEFAULT_XML_OBJECT_MAPPER = new DefaultXmlObjectMapper();

    private DefaultXmlObjectMapper() {
        super();
    }
}
