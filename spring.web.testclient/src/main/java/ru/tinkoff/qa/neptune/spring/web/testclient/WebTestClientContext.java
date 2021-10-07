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

    public WebTestClientContext webTestClient(SendRequestAction<?> sending) {
        return super.perform(sending);
    }

    public <T> T webTestClient(SequentialGetStepSupplier<WebTestClientContext, T, ?, ?, ?> sending) {
        return super.get(sending);
    }
}
