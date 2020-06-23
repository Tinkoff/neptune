package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.tinkoff.qa.neptune.http.api.mapper.DefaultBodyMappers;

import static java.util.Optional.ofNullable;

public enum BodyDataFormat {
    /**
     * This item is designed to return request body objects as they are.
     */
    NONE {
        @Override
        public Object format(Object object) {
            return object;
        }
    },

    /**
     * This item is designed to return json-formatted string. This sting is created
     * when it is necessary to pass objects that differ from {@link ru.tinkoff.qa.neptune.http.api.dto.JsonDTObject}
     * as json bodies of http requests.
     */
    XML {
        @Override
        public String format(Object object) {
            return ofNullable(object)
                    .map(o -> {
                        try {
                            return DefaultBodyMappers.XML.getMapper().writeValueAsString(o);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElse(null);
        }
    },

    /**
     * This item is designed to return xml-formatted string. This sting is created
     * when it is necessary to pass objects that differ from {@link ru.tinkoff.qa.neptune.http.api.dto.XmlDTObject}
     * as xml bodies of http requests.
     */
    JSON {
        @Override
        public String format(Object object) {
            return ofNullable(object)
                    .map(o -> {
                        try {
                            return DefaultBodyMappers.JSON.getMapper().writeValueAsString(o);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElse(null);
        }
    },

    /**
     * This item is designed to return {@link Document}. It is created
     * when it is necessary to pass objects that differ from {@link Document}
     * as html/xml bodies of http requests.
     */
    HTML {
        @Override
        public Document format(Object object) {
            return ofNullable(object).flatMap(o -> ofNullable(o)
                    .map(o1 -> Jsoup.parse(String.valueOf(o))))
                    .orElse(null);
        }
    };

    public abstract Object format(Object object);
}
