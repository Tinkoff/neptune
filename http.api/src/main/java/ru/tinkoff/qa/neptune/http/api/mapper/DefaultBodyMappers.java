package ru.tinkoff.qa.neptune.http.api.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultJsonDTObjectMapper;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultXmlDTObjectMapper;

import java.util.Objects;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultJsonDTObjectMapper.DEFAULT_JSON_DT_OBJECT_MAPPER;
import static ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultXmlDTObjectMapper.DEFAULT_XML_DT_OBJECT_MAPPER;

/**
 * Default mappers of serialized http request/response bodies.
 * {@link ObjectMapper} is used to serialize objects to json string.
 * {@link XmlMapper} is used to serialize objects to json string.
 *
 * @see DefaultJsonDTObjectMapper
 * @see DefaultXmlDTObjectMapper
 */
public enum DefaultBodyMappers {
    JSON {
        @Override
        public ObjectMapper getMapper() {
            return ofNullable(DEFAULT_JSON_DT_OBJECT_MAPPER.get())
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
            return ofNullable(DEFAULT_XML_DT_OBJECT_MAPPER.get())
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
