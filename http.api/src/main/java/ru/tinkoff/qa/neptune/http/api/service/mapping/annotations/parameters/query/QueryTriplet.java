package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query;

/**
 * It is a POGO for the query parameter transferring.
 */
public class QueryTriplet {

    private final String name;
    private final boolean toExpand;
    private final Object[] params;

    QueryTriplet(String name, boolean toExpand, Object... params) {
        this.name = name;
        this.toExpand = toExpand;
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
    public Object[] getParams() {
        return params;
    }
}
