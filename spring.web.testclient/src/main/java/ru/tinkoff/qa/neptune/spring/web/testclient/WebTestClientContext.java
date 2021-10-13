package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.test.web.reactive.server.WebTestClient;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;

public class WebTestClientContext extends Context<WebTestClientContext> {

    private static final WebTestClientContext context = getInstance(WebTestClientContext.class);
    private WebTestClient defaultWebTestClient;

    static WebTestClientContext getContext() {
        return context;
    }

    void setDefault(WebTestClient defaultWebTestClient) {
        this.defaultWebTestClient = defaultWebTestClient;
    }

    WebTestClient getDefaultWebTestClient() {
        checkState(nonNull(defaultWebTestClient), "There is no field of type WebTestClient that has a non-null value");
        return defaultWebTestClient;
    }

    /**
     * Sends specified request and receives a response
     *
     * @param sending is specification of request
     * @return self-reference
     */
    public static WebTestClientContext webTestClient(SendRequestAction<?> sending) {
        var context = getContext();
        return context.perform(sending);
    }

    /**
     * Gets some value from body of response.
     *
     * @param whatToGet is specification of value to get
     * @return value taken from / calculated by response body
     */
    public static <T> T webTestClient(SequentialGetStepSupplier<WebTestClientContext, T, ?, ?, ?> whatToGet) {
        var context = getContext();
        return context.get(whatToGet);
    }
}
