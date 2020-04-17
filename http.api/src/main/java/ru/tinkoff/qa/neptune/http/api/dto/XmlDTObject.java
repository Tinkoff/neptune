package ru.tinkoff.qa.neptune.http.api.dto;

import static ru.tinkoff.qa.neptune.http.api.mapper.DefaultBodyMappers.XML;

public class XmlDTObject extends DTObject {

    /**
     * Transforms object to xml string.
     * Annotations of the {@link javax.xml.bind.annotation} package are allowed for xml's.
     * </p>
     *
     * @return String value of serialized object
     */
    public String serialize() {
        try {
            return XML.getMapper().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
