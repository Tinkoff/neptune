package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.test.web.reactive.server.WebTestClient;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.spring.boot.starter.web.testclient.WebTestClientProvider;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;
import static java.util.ServiceLoader.load;
import static java.util.stream.StreamSupport.stream;

public class WebTestClientContext extends Context<WebTestClientContext> {

    private static final WebTestClientContext context = getInstance(WebTestClientContext.class);
    private final WebTestClientProvider defaultWebTestClientProvider;

    public WebTestClientContext() {
        var iterator = load(WebTestClientProvider.class).iterator();
        Iterable<WebTestClientProvider> iterable = () -> iterator;
        this.defaultWebTestClientProvider = stream(iterable.spliterator(), false)
                .findFirst()
                .orElse(null);
    }

    static WebTestClientContext getContext() {
        return context;
    }

    WebTestClient getDefaultWebTestClient() {
        checkState(nonNull(defaultWebTestClientProvider), "There is no provider of service " + WebTestClientProvider.class);
        var defaultWebTestClient = defaultWebTestClientProvider.provide();
        checkState(nonNull(defaultWebTestClient), "The instance of provider "
                + defaultWebTestClientProvider.getClass()
                + " of service "
                + WebTestClientProvider.class
                + " returned null");
        return defaultWebTestClient;
    }

    /**
     * Sends specified request and receives a response
     *
     * @param sending is specification of request
     * @return self-reference
     */
    public static WebTestClientContext webTestClient(SendRequestAction<?, ?, ?> sending) {
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
