package ru.tinkoff.qa.neptune.http.api.mapping;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultJsonObjectMapper;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultXmlObjectMapper;

import java.util.Objects;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.properties.date.format.ApiDateFormatProperty.API_DATE_FORMAT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultJsonObjectMapper.DEFAULT_JSON_OBJECT_MAPPER;
import static ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultXmlObjectMapper.DEFAULT_XML_OBJECT_MAPPER;

/**
 * This is deprecated in favour of usage of {@link ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer}
 */
@Deprecated
/**
 * Default mappers of serialized/deserialized http request/response bodies.
 *
 * @see DefaultJsonObjectMapper
 * @see DefaultXmlObjectMapper
 */
public enum DefaultMapper {
    /**
     * Gets/creates default {@link ObjectMapper} to serialize objects to/deserialize from json-string
     */
    JSON {
        @Override
        public ObjectMapper getMapper() {
            var m = ofNullable(DEFAULT_JSON_OBJECT_MAPPER.get()).orElseGet(ObjectMapper::new);
            addModuleIfNecessary(m, ParameterNamesModule.class, new ParameterNamesModule());
            addModuleIfNecessary(m, Jdk8Module.class, new Jdk8Module());
            addModuleIfNecessary(m, JavaTimeModule.class, new JavaTimeModule());
            ofNullable(API_DATE_FORMAT_PROPERTY.get()).ifPresent(m::setDateFormat);
            return m;
        }
    },

    /**
     * Gets/creates default {@link XmlMapper} to serialize objects to/deserialize from xml-string
     */
    XML {
        @Override
        public ObjectMapper getMapper() {
            var m = ofNullable(DEFAULT_XML_OBJECT_MAPPER.get()).orElseGet(XmlMapper::new);
            addModuleIfNecessary(m, JaxbAnnotationModule.class, new JaxbAnnotationModule());
            addModuleIfNecessary(m, JacksonXmlModule.class, new JacksonXmlModule());
            addModuleIfNecessary(m, ParameterNamesModule.class, new ParameterNamesModule());
            addModuleIfNecessary(m, Jdk8Module.class, new Jdk8Module());
            addModuleIfNecessary(m, JavaTimeModule.class, new JavaTimeModule());
            if (m.getDateFormat() == null) {
                ofNullable(API_DATE_FORMAT_PROPERTY.get()).ifPresent(m::setDateFormat);
            }
            return m;
        }
    };

    private static <T extends Module> void addModuleIfNecessary(ObjectMapper mapper, Class<T> classModule, T toSet) {
        var moduleIds = mapper.getRegisteredModuleIds();

        if (moduleIds
                .stream()
                .noneMatch(o -> Objects.equals(String.valueOf(o), classModule.getName()))) {
            mapper.registerModule(toSet);
        }
    }

    public abstract ObjectMapper getMapper();
}
