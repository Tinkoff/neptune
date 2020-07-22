package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form;

import ru.tinkoff.qa.neptune.http.api.request.FormValueDelimiters;

/**
 * It is a POGO for the form parameter transferring.
 */
public class ReadFormParameter {

    private final String name;
    private final boolean toExpand;
    private final FormValueDelimiters delimiter;
    private final boolean allowReserved;
    private final Object[] params;

    ReadFormParameter(String name, boolean toExpand, FormValueDelimiters delimiter, boolean allowReserved, Object... params) {
        this.name = name;
        this.toExpand = toExpand;
        this.delimiter = delimiter;
        this.allowReserved = allowReserved;
        this.params = params;
    }

    /**
     * @return name of a form parameter
     */
    public String getName() {
        return name;
    }

    /**
     * @return to expand or not expand values of the form parameter
     */
    public boolean isToExpand() {
        return toExpand;
    }

    /**
     * @return values of the form parameter
     */
    public Object[] getValues() {
        return params;
    }

    /**
     * @return delimiter for not exploded values
     */
    public FormValueDelimiters getDelimiter() {
        return delimiter;
    }

    /**
     * @return to keep reserved characters not encoded or not
     */
    public boolean isAllowReserved() {
        return allowReserved;
    }
}
