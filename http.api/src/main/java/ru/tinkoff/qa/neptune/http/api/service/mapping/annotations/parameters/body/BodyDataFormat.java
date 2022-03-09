package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body;

import ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper;

import java.util.Date;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.properties.date.format.ApiDateFormatProperty.API_DATE_FORMAT_PROPERTY;

public enum BodyDataFormat {
    /**
     * This item is designed to return request body objects as they are.
     */
    NONE {
        @Override
        public Object format(Object object, Class<?>... mixIns) {
            if (object instanceof Date) {
                return ofNullable(API_DATE_FORMAT_PROPERTY.get())
                        .map(simpleDateFormat -> (Object) simpleDateFormat.format((Date) object))
                        .orElse(object);
            }

            return object;
        }
    },

    /**
     * This item is designed to return xml-formatted string
     */
    XML {
        @Override
        public String format(Object object, Class<?>... mixIns) {
            return ofNullable(object)
                    .map(o -> {
                        try {
                            var copy = DefaultMapper.XML
                                    .getMapper()
                                    .copy();

                            stream(mixIns).forEach(aClass -> copy.addMixIn(object.getClass(), aClass));
                            return copy.writeValueAsString(o);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElse(null);
        }
    },

    /**
     * This item is designed to return json-formatted string
     */
    JSON {
        @Override
        public String format(Object object, Class<?>... mixIns) {
            return ofNullable(object)
                    .map(o -> {
                        try {
                            var copy = DefaultMapper.JSON
                                    .getMapper()
                                    .copy();

                            stream(mixIns).forEach(aClass -> copy.addMixIn(object.getClass(), aClass));
                            return copy.writeValueAsString(o);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElse(null);
        }
    };

    public abstract Object format(Object object, Class<?>... mixIns);
}
