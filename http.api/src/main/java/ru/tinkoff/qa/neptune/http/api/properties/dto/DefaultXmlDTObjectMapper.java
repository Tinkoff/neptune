package ru.tinkoff.qa.neptune.http.api.properties.dto;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

/**
 * This class is designed to read value of the property {@code 'DEFAULT_XML_DTO_MAPPER'} and convert it to an instance of
 * a {@link Supplier} that creates {@link com.fasterxml.jackson.dataformat.xml.XmlMapper}
 */
public final class DefaultXmlDTObjectMapper implements ObjectPropertySupplier<Supplier<XmlMapper>> {

    public static final DefaultXmlDTObjectMapper DEFAULT_XML_DT_OBJECT_MAPPER = new DefaultXmlDTObjectMapper();
    private static final String PROPERTY = "DEFAULT_XML_DTO_MAPPER";

    private DefaultXmlDTObjectMapper() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }
}
