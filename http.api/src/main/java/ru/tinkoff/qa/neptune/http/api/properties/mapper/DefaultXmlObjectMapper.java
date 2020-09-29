package ru.tinkoff.qa.neptune.http.api.properties.mapper;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

/**
 * This class is designed to read value of the property {@code 'DEFAULT_XML_MAPPER'} and convert it to an instance of
 * a {@link Supplier} that creates {@link com.fasterxml.jackson.dataformat.xml.XmlMapper}
 */
public final class DefaultXmlObjectMapper implements ObjectPropertySupplier<Supplier<XmlMapper>> {

    public static final DefaultXmlObjectMapper DEFAULT_XML_OBJECT_MAPPER = new DefaultXmlObjectMapper();
    private static final String PROPERTY = "DEFAULT_XML_MAPPER";

    private DefaultXmlObjectMapper() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }
}
