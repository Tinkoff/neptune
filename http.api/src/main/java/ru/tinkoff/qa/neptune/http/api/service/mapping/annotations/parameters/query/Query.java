package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query;

import ru.tinkoff.qa.neptune.http.api.request.QueryValueDelimiters;

/**
 * It is a POGO for the query parameter transferring.
 */
public class Query {

    private final String name;
    private final boolean toExpand;
    private final QueryValueDelimiters delimiter;
    private final Object[] params;

    Query(String name, boolean toExpand, QueryValueDelimiters delimiter, Object... params) {
        this.name = name;
        this.toExpand = toExpand;
        this.delimiter = delimiter;
        this.params = params;
    }

    /**
     * @return name of a query parameter
     */
    public String getName() {
        return name;
    }

    /**
     * @return to expand or not expand values of the query parameter
     */
    public boolean isToExpand() {
        return toExpand;
    }

    /**
     * @return values of the query parameter
     */
    public Object[] getValues() {
        return params;
    }

    /**
     * @return delimiter for not exploded values
     */
    public QueryValueDelimiters getDelimiter() {
        return delimiter;
    }
}
