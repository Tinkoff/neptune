package ru.tinkoff.qa.neptune.http.api.request;

/**
 * Helps to tune http request by {@link RequestSettings}.
 *
 * @see RequestTuner#setUp(RequestSettings)
 */
public interface RequestTuner {

    /**
     * Tune a resulted http request by {@link RequestSettings}
     *
     * @param requestSettings to be modified
     */
    void setUp(RequestSettings<?> requestSettings);
}
