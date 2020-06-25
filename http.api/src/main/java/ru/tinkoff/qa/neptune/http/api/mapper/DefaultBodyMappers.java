package ru.tinkoff.qa.neptune.http.api.mapper;

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
     */
    JSON {
        @Override
        public ObjectMapper getMapper() {
            return ofNullable(DEFAULT_JSON_OBJECT_MAPPER.get())
                    .map(s -> ofNullable(s.get())
                            .map(mapper -> {
                                addModuleIfNecessary(mapper, ParameterNamesModule.class, new ParameterNamesModule());
                                addModuleIfNecessary(mapper, Jdk8Module.class, new Jdk8Module());
                                addModuleIfNecessary(mapper, JavaTimeModule.class, new JavaTimeModule());
                                return mapper;
                            })
                            .orElseThrow(
                                    () -> new IllegalStateException(format("An instance of %s supplied null-value",
                                            s.getClass().getName()))
                            ))
                    .orElseGet(() -> {
                        var mapper = new ObjectMapper();
                        addModuleIfNecessary(mapper, ParameterNamesModule.class, new ParameterNamesModule());
                        addModuleIfNecessary(mapper, Jdk8Module.class, new Jdk8Module());
                        addModuleIfNecessary(mapper, JavaTimeModule.class, new JavaTimeModule());
                        return mapper;
                    });
        }
    },

    /**
     * Gets/creates default {@link XmlMapper} to serialize objects to/deserialize from xml-string
     */
    XML {
        @Override
        public ObjectMapper getMapper() {
            return ofNullable(DEFAULT_XML_OBJECT_MAPPER.get())
                    .map(s -> ofNullable(s.get())
                            .map(xmlMapper -> {
                                addModuleIfNecessary(xmlMapper, JaxbAnnotationModule.class, new JaxbAnnotationModule());
                                addModuleIfNecessary(xmlMapper, JacksonXmlModule.class, new JacksonXmlModule());
                                addModuleIfNecessary(xmlMapper, ParameterNamesModule.class, new ParameterNamesModule());
                                addModuleIfNecessary(xmlMapper, Jdk8Module.class, new Jdk8Module());
                                addModuleIfNecessary(xmlMapper, JavaTimeModule.class, new JavaTimeModule());
                                return xmlMapper;
                            })
                            .orElseThrow(
                                    () -> new IllegalStateException(format("An instance of %s supplied null-value",
                                            s.getClass().getName()))
                            ))
                    .orElseGet(() -> {
                        var mapper = new XmlMapper();
                        addModuleIfNecessary(mapper, JaxbAnnotationModule.class, new JaxbAnnotationModule());
                        addModuleIfNecessary(mapper, JacksonXmlModule.class, new JacksonXmlModule());
                        addModuleIfNecessary(mapper, ParameterNamesModule.class, new ParameterNamesModule());
                        addModuleIfNecessary(mapper, Jdk8Module.class, new Jdk8Module());
                        addModuleIfNecessary(mapper, JavaTimeModule.class, new JavaTimeModule());
                        return mapper;
                    });
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
