package ru.tinkoff.qa.neptune.http.api.dto;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import java.util.Objects;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.properties.dto.DefaultXmlDTObjectMapper.DEFAULT_XML_DT_OBJECT_MAPPER;

public class XmlDTObject extends DTObject {

    private static XmlMapper xmlObjectMapper() {
        return ofNullable(DEFAULT_XML_DT_OBJECT_MAPPER.get())
                .map(s -> ofNullable(s.get())
                        .map(xmlMapper -> {
                            if (xmlMapper
                                    .getRegisteredModuleIds()
                                    .stream()
                                    .noneMatch(o -> Objects.equals(valueOf(o), JaxbAnnotationModule.class.getName()))) {
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

    /**
     * Transforms object to xml string.
     * Annotations of the {@link javax.xml.bind.annotation} package are allowed for xml's.
     * </p>
     *
     * @return String value of serialized object
     */
    public String serialize() {
        try {
            return xmlObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
