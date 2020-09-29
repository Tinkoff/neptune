package ru.tinkoff.qa.neptune.http.api.request.body.url.encoded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper;
import ru.tinkoff.qa.neptune.http.api.request.FormValueDelimiters;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class represents a form parameter.
 */
public final class FormParameter {

    private final String name;
    private final Object[] values;
    private final boolean toExpand;
    private final FormValueDelimiters delimiter;
    private final boolean allowReserved;
    private final boolean toNotEncodeValue;

    private FormParameter(String name,
                          boolean toExpand,
                          FormValueDelimiters delimiter,
                          boolean allowReserved,
                          boolean toNotEncodeValue,
                          Object... values) {
        this.name = name;
        this.toExpand = toExpand;
        this.delimiter = delimiter;
        this.allowReserved = allowReserved;
        this.toNotEncodeValue = toNotEncodeValue;
        this.values = values;
    }

    /**
     * Creates a parameter with encoded and exploded values.
     *
     * @param name          is a parameter name
     * @param allowReserved allows to use reserved character not encoded or not
     * @param values        values of the defined parameter
     * @return an instance of {@link FormParameter}
     */
    public static FormParameter formParameter(String name,
                                              boolean allowReserved,
                                              Object... values) {
        return new FormParameter(name,
                true,
                null,
                allowReserved,
                false,
                values);
    }

    /**
     * Creates a parameter with encoded and not exploded values.
     *
     * @param name          is a parameter name
     * @param delimiter     is a delimiter of array/iterable value
     * @param allowReserved allows to use reserved character not encoded or not
     * @param values        values of the defined parameter
     * @return an instance of {@link FormParameter}
     */
    public static FormParameter formParameter(String name,
                                              FormValueDelimiters delimiter,
                                              boolean allowReserved,
                                              Object... values) {
        return new FormParameter(name,
                false,
                delimiter,
                allowReserved,
                false,
                values);
    }

    /**
     * Creates a parameter with not encoded values.
     *
     * @param name   is a parameter name
     * @param mapper is an object of {@link ObjectMapper} that serializes value of the parameter
     * @param value  is a value of the defined parameter
     * @return an instance of {@link FormParameter}
     */
    public static FormParameter formParameter(String name,
                                              ObjectMapper mapper,
                                              Object value) {
        checkNotNull(mapper);
        checkNotNull(value);
        try {
            return formParameter(name, mapper.writeValueAsString(value));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a parameter with not encoded values.
     *
     * @param name   is a parameter name
     * @param mapper is an item of {@link DefaultMapper} that serializes value of the parameter
     * @param value  is a value of the defined parameter
     * @return an instance of {@link FormParameter}
     */
    public static FormParameter formParameter(String name,
                                              DefaultMapper mapper,
                                              Object value) {
        return formParameter(name, mapper.getMapper(), value);
    }

    /**
     * Creates a parameter with not encoded values.
     *
     * @param name  is a parameter name
     * @param value is a value of the defined parameter
     * @return an instance of {@link FormParameter}
     */
    public static FormParameter formParameter(String name,
                                              Object value) {
        checkNotNull(value);
        return new FormParameter(name,
                true,
                null,
                true,
                true,
                value);
    }

    boolean isAllowReserved() {
        return allowReserved;
    }

    String getName() {
        return name;
    }

    boolean isToExpand() {
        return toExpand;
    }

    FormValueDelimiters getDelimiter() {
        return delimiter;
    }

    boolean isToNotEncodeValue() {
        return toNotEncodeValue;
    }

    Object[] getValues() {
        return values;
    }
}
