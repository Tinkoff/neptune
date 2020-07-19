package ru.tinkoff.qa.neptune.http.api.request;

/**
 * Delimiters of not exploded values of http query parameter.
 */
public enum QueryValueDelimiters {
    /**
     * For comma-separated values
     */
    COMMA(","),
    /**
     * For space-separated values
     */
    SPACE("%20"),
    /**
     * For pipe-separated values
     */
    PIPE("%7C");

    private final String delimiter;

    QueryValueDelimiters(String delimiter) {
        this.delimiter = delimiter;
    }

    public String toString() {
        return delimiter;
    }
}
