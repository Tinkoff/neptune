package ru.tinkoff.qa.neptune.http.api.request;

/**
 * Delimiters of not exploded values of http forms.
 */
public enum FormValueDelimiters {
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

    FormValueDelimiters(String delimiter) {
        this.delimiter = delimiter;
    }

    public String toString() {
        return delimiter;
    }
}
