package ru.tinkoff.qa.neptune.http.api.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultJsonObjectMapper;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultXmlObjectMapper;

import java.util.Objects;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultJsonObjectMapper.DEFAULT_JSON_OBJECT_MAPPER;
import static ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultXmlObjectMapper.DEFAULT_XML_OBJECT_MAPPER;

/**
 * Default mappers of serialized/deserialized http request/response bodies.
 *
 * @see DefaultJsonObjectMapper
 * @see DefaultXmlObjectMapper
 */
public enum DefaultBodyMappers {
    /**
     * Gets/creates default {@link ObjectMapper} to serialize objects to/deserialize from json-string
     *
     * @see
     */
    JSON {
        @Override
        public ObjectMapper getMapper() {
            return ofNullable(DEFAULT_JSON_OBJECT_MAPPER.get())
                    .map(s -> ofNullable(s.get())
                            .orElseThrow(
                                    () -> new IllegalStateException(format("An instance of %s supplied null-value",
                                            s.getClass().getName()))
                            ))
                    .orElseGet(ObjectMapper::new);
        }
    },
    XML {
        @Override
        public ObjectMapper getMapper() {
            return ofNullable(DEFAULT_XML_OBJECT_MAPPER.get())
                    .map(s -> ofNullable(s.get())
                            .map(xmlMapper -> {
                                if (xmlMapper
                                        .getRegisteredModuleIds()
                                        .stream()
                                        .noneMatch(o -> Objects.equals(String.valueOf(o), JaxbAnnotationModule.class.getName()))) {
                                    xmlMapper.registerModule(new JaxbAnnotationModule());
                                }
                                return xmlMapper;
                            })
                            .orElseThrow(
                                    () -> new IllegalStateException(format("An instance of %s supplied null-value",
                                            s.getClass().getName()))
                            ))
                    .orElseGet(() -> {
                        var mapper = new XmlMapper();
                        mapper.registerModule(new JaxbAnnotationModule());
                        return mapper;
                    });
        }
    };

    public abstract ObjectMapper getMapper();
}
